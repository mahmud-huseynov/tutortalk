import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import TokenManager from '../api/TokenManager';
import styles from './ChatRoom.module.css';
import UserRoleApi from '../api/UserRoleApi';

let stompClient = null;

const ChatRoom = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { username, recipient } = location.state || { username: TokenManager.getUsername(), recipient: { name: 'Chatroom', role: 'General' } };
    const [privateChats, setPrivateChats] = useState(new Map());
    const [tab, setTab] = useState(recipient.name);
    const [userData, setUserData] = useState({
        username: username,
        connected: false,
        message: '',
        role: '',
        userId: null
    });

    const currentDate = new Date();

    useEffect(() => {
        if (!userData.username) {
            return;
        }

        connect();
        fetchMessages();
        fetchUserIdAndRole();
    }, [userData.username]);

    const connect = () => {
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock);
        stompClient.connect({}, onConnected, onError);
    };

    const fetchUserIdAndRole = () => {
        // Fetch user ID first
        axios.get(`http://localhost:8080/users/username/${userData.username}`)
            .then(response => {
                const userId = response.data.id; // Assuming response.data has id field
                setUserData(prevData => ({
                    ...prevData,
                    userId: userId
                }));

                // Then fetch user role using the fetched userId
                axios.get(`http://localhost:8080/user-roles/${userId}`)
                    .then(roleResponse => {
                        setUserData(prevData => ({
                            ...prevData,
                            role: roleResponse.data.role // Assuming roleResponse.data has role field
                        }));
                    })
                    .catch(error => {
                        console.error("Error fetching user role:", error);
                    });
            })
            .catch(error => {
                console.error("Error fetching user ID:", error);
                console.log(error.response); // Log full response object for detailed error message
            });
    };


    

    const fetchMessages = () => {
        axios.get('http://localhost:8080/messages/all', {
            params: {
                username: userData.username
            }
        })
            .then(response => {
                const allMessages = response.data;

                const privateMessages = allMessages.filter(message =>
                    message.status === "MESSAGE" &&
                    (message.senderName === userData.username || message.receiverName === userData.username)
                );
                const privateChatsMap = new Map();
                privateMessages.forEach(message => {
                    const otherParty = message.senderName === userData.username ? message.receiverName : message.senderName;
                    if (!privateChatsMap.has(otherParty)) {
                        privateChatsMap.set(otherParty, []);
                    }
                    if (!privateChatsMap.get(otherParty).some(msg => msg.id === message.id)) {
                        privateChatsMap.get(otherParty).push(message);
                    }
                });

                // Ensure the selected recipient is in the private chats map
                if (!privateChatsMap.has(recipient.name)) {
                    privateChatsMap.set(recipient.name, []);
                }

                setPrivateChats(privateChatsMap);
            })
            .catch(error => {
                console.error("Error fetching messages:", error);
            });
    };

    const onConnected = () => {
        setUserData(prevData => ({ ...prevData, connected: true }));
        stompClient.subscribe(`/user/${userData.username}/private`, onPrivateMessage);
        userJoin();
    };

    const userJoin = () => {
        var chatMessage = {
            senderName: userData.username,
            status: "JOIN"
        };
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    };

    const onPrivateMessage = (payload) => {
        var payloadData = JSON.parse(payload.body);
        if (payloadData.status === "MESSAGE") {
            const otherParty = payloadData.senderName === userData.username ? payloadData.receiverName : payloadData.senderName;
            setPrivateChats(prevChats => {
                const updatedChats = new Map(prevChats);
                if (updatedChats.has(otherParty)) {
                    if (!updatedChats.get(otherParty).some(msg => msg.id === payloadData.id)) {
                        updatedChats.get(otherParty).push(payloadData);
                    }
                } else {
                    updatedChats.set(otherParty, [payloadData]);
                }
                return updatedChats;
            });
        }
    };

    const onError = (err) => {
        console.log(err);
    };

    const handleMessage = (event) => {
        const { value } = event.target;
        setUserData(prevData => ({ ...prevData, message: value }));
    };

    const sendPrivateValue = () => {
        if (stompClient && userData.message && tab !== "CHATROOM") {
            if (userData.username === tab) {
                alert("You cannot message yourself.");
                return;
            }

            var chatMessage = {
                senderName: userData.username,
                receiverName: tab,
                message: userData.message,
                status: "MESSAGE",
                date: currentDate.toISOString()
            };

            axios.post('http://localhost:8080/messages/save', chatMessage)
                .then(response => {
                    console.log("Private message saved successfully:", response.data);

                    setPrivateChats(prevChats => {
                        const updatedChats = new Map(prevChats);
                        const messages = updatedChats.get(tab) || [];
                        return updatedChats.set(tab, [...messages, chatMessage]);
                    });

                    stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
                    setUserData(prevData => ({ ...prevData, message: "" }));
                })
                .catch(error => {
                    console.error("Error saving private message:", error);
                });
        }
    };

    return (
        <div className={styles.container}>
            {userData.connected ?
                <div className={styles['chat-box']}>
                    <div className={styles['member-list']}>
                    <ul>
  {[...privateChats.keys()].map((name, index) => (
    <li
      onClick={() => { setTab(name) }}
      className={`member ${tab === name ? "active" : ""}`}
      key={index}
    >
      {name}
    </li>
  ))}
</ul>

                    </div>
                    <div className={styles['chat-content-wrapper']}>
                        <div className={styles['chat-header']}>
                            <h2>{tab}</h2>
                        </div>
                        <div className={styles['chat-content']}>
                            <ul className={styles['chat-messages']}>
                                {privateChats.get(tab)?.map((chat, index) => (
                                    <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>
                                        <div className={styles.sender}>{chat.senderName}</div>
                                        <div className={styles['message-data']}>{chat.message}</div>
                                    </li>
                                ))}
                            </ul>
                        </div>
                        <div className={styles['send-message']}>
                            <input type="text" className={styles['input-message']} placeholder="Enter your message" value={userData.message} onChange={handleMessage} />
                            <button type="button" className={styles['send-button']} onClick={sendPrivateValue}>Send</button>
                        </div>
                    </div>
                </div>
                :
                <div className={styles.register}>
                    <p>Connecting...</p>
                </div>
            }
        </div>
    );
};

export default ChatRoom;
