<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Topic</h1>
    <form id="ruleForm">
        <div class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <input type="text" class="form-control" id="topicInput" name="topic" required>
        </div>
        <button type="button" id="submitButton" class="btn btn-primary">Create Rule</button>
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
</script>