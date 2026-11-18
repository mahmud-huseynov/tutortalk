import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import TokenManager from '../api/TokenManager';
import TutorApi from '../api/TutorApi';
import styles from './InboxPage.module.css';

const InboxPage = () => {
    const [messages, setMessages] = useState([]);
    const username = TokenManager.getUsername();
    const [userId, setUserId] = useState(null);
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        // Establish WebSocket connection
        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(() => socket);
        client.reconnect_delay = 5000;
        client.connect({}, () => {
            setStompClient(client);
        }, error => {
            console.error('Error connecting to WebSocket:', error);
        });

        return () => {
            // Disconnect WebSocket when the component unmounts
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    useEffect(() => {
        // Fetch user ID
        TutorApi.getUserIdByUsername(username)
            .then(id => setUserId(id))
            .catch(error => console.error('Error fetching user ID:', error));
    }, [username]);

    useEffect(() => {
        if (userId && stompClient) {
            const subscription = stompClient.subscribe(`/user/${username}/queue/messages`, message => {
                const messageData = JSON.parse(message.body);
                setMessages(prevMessages => [...prevMessages, messageData]);
            });

            // Unsubscribe from the subscription when the component unmounts
            return () => {
                subscription.unsubscribe();
            };
        }
    }, [stompClient, userId, username]);

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <h1>Inbox</h1>
            </header>
            <section className={styles.messageList}>
                {messages.map((message, index) => (
                    <div key={index} className={styles.messageItem}>
                        <h2>From: {message.senderId}</h2>
                        <p>{message.content}</p>
                    </div>
                ))}
            </section>
        </div>
    );
}

export default InboxPage;
