<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Rule</h1>
    <form id="ruleForm">
        <div class="mb-3">
            <label for="ruleInput" class="form-label">Rule:</label>
            <input type="text" class="form-control" id="ruleInput" name="rule" required>
        </div>
        <div class="mb-3">
            <label for="linkInput" class="form-label">Link:</label>
            <input type="text" class="form-control" id="linkInput" name="link" required>
        </div>
        <button type="button" id="submitButton" class="btn btn-primary">Create Rule</button>
    </form>
</div>

<div id="messageContainer" class="container mt-4"></div>

<script>
    document.getElementById('submitButton').addEventListener('click', function() {
        var ruleInput = document.getElementById('ruleInput').value;
        var linkInput = document.getElementById('linkInput').value;

        var requestData = {
            rule: ruleInput,
            link: linkInput
        };

        // Send AJAX request with JSON data
        fetch('/rules', {
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
                message.textContent = 'Rule has been added successfully';
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
        document.getElementById('ruleInput').value = '';
        document.getElementById('linkInput').value = '';
    }
</script>