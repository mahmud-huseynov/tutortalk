const TutorDetails = ({ tutorDetails }) => {
    return (
        <div>
            <p>Tutor Details:</p>
            <ul>
                <li>ID: {tutorDetails.id}</li>
                <li>Name: {studentDetails.name}</li>
                <li>Languages: {tutorDetails.languages}</li>
            </ul>
        </div>
    );
};

export default TutorDetails;