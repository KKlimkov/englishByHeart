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
        <div id="rulesContainer" class="container mt-4 row"></div>
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
                fetchRules();
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

    // Function to fetch and display rules
    function fetchRules() {
        fetch('http://localhost:8080/rulesByUserId?userId=1')
            .then(response => response.json())
            .then(data => {
                var rulesContainer = document.getElementById('rulesContainer');
                rulesContainer.innerHTML = ''; // Clear existing rules

                data.forEach(rule => {
                    var card = createRuleCard(rule);
                    rulesContainer.appendChild(card);
                });
            })
            .catch(error => {
                console.error('Error fetching rules:', error);
            });
    }

    // Function to create a rule card
    function createRuleCard(rule) {
        const card = document.createElement('div');
        card.className = 'col-md-4';

        const cardContent = `
            <div class="card mb-4">
                <div class="card-body">
                    <h5 class="card-title"></h5>
                    <p class="card-text"><strong>Link:</strong> </p>
                    <p class="card-text"><strong>User ID:</strong> </p>
                </div>
            </div>
        `;

        card.innerHTML = cardContent;
        card.querySelector('.card-title').textContent = rule.rule;
        card.querySelector('.card-text:nth-of-type(1)').append(document.createTextNode(rule.link));
        card.querySelector('.card-text:nth-of-type(2)').append(document.createTextNode(rule.userId));

        return card;
    }

    // Initial fetch of rules when the page loads
    fetchRules();

</script>