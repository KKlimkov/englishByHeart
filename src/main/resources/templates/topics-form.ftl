<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Topic</h1>
    <form id="ruleForm">
        <div class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <input type="text" class="form-control" id="topicInput" name="topic" required>
        </div>
        <button type="button" id="submitButton" class="btn btn-primary">Create Rule</button>
        <div id="topicsContainer" class="container mt-4 row"></div>
    </form>
</div>

<div id="messageContainer" class="container mt-4"></div>

<script>
    document.getElementById('submitButton').addEventListener('click', function() {
        var topicInput = document.getElementById('topicInput').value;
        var requestData = {
            userId: 1, // Assuming the user ID is fixed or you can obtain it dynamically
            topicName: topicInput
        };

        // Send AJAX request with JSON data
        fetch('/topics', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            var messageContainer = document.getElementById('messageContainer');
            if (response.ok) {
                var message = document.createElement('div');
                message.classList.add('alert', 'alert-success');
                message.textContent = 'Topic has been added successfully';
                messageContainer.innerHTML = '';
                messageContainer.appendChild(message);
                clearForm(); // Clear form fields after successful submission
                fetchTopics();
            } else {
                throw new Error('Unexpected error');
            }
        })
        .catch(error => {
            var messageContainer = document.getElementById('messageContainer');
            var message = document.createElement('div');
            message.classList.add('alert', 'alert-danger');
            message.textContent = error.message;
            messageContainer.innerHTML = '';
            messageContainer.appendChild(message);
        });
    });

    // Function to clear form fields
    function clearForm() {
        document.getElementById('topicInput').value = '';
    }

    // Function to fetch and display topics
    function fetchTopics() {
        fetch('http://localhost:8080/topics?userId=1')
            .then(response => response.json())
            .then(data => {
                var topicsContainer = document.getElementById('topicsContainer');
                topicsContainer.innerHTML = ''; // Clear existing topics

                data.forEach(topic => {
                    var card = createTopicCard(topic);
                    topicsContainer.appendChild(card);
                });
            })
            .catch(error => {
                console.error('Error fetching topics:', error);
            });
    }

    // Function to create a topic card
    function createTopicCard(topic) {
        const card = document.createElement('div');
        card.className = 'col-md-4';

        const cardContent = `
            <div class="card mb-4">
                <div class="card-body">
                    <h5 class="card-title"></h5>
                    <p class="card-text"><strong>User ID:</strong> </p>
                </div>
            </div>
        `;

        card.innerHTML = cardContent;
        card.querySelector('.card-title').textContent = topic.topicName;
        card.querySelector('.card-text:nth-of-type(1)').append(document.createTextNode(topic.userId));

        return card;
    }

    // Initial fetch of topics when the page loads
    fetchTopics();

</script>