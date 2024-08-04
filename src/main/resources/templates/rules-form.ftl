<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Rule</h1>
    <div id="ruleForm">
        <div class="mb-3">
            <label for="ruleInput" class="form-label">Rule:</label>
            <input type="text" class="form-control" id="ruleInput" name="rule" autocomplete="off" required>
        </div>
        <div class="mb-3">
            <label for="linkInput" class="form-label">Link:</label>
            <input type="text" class="form-control" id="linkInput" name="link" autocomplete="off" required>
        </div>
        <div class="mb-3">
            <label for="sortBy" class="form-label">Sort By:</label>
            <select id="sortBy" class="form-select">
                <option value="CREATE_DATE">Create Date</option>
                <option value="UPDATE_DATE">Update Date</option>
                <option value="NAME">Name</option>
                <option value="LINK">Link</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="sortDirection" class="form-label">Direction:</label>
            <select id="sortDirection" class="form-select">
                <option value="ASC">Ascending</option>
                <option value="DESC">Descending</option>
            </select>
        </div>
        <button type="button" id="applyButton" class="btn btn-primary">Apply</button>
        <button type="button" id="submitButton" class="btn btn-primary" disabled>Create Rule</button>
        <div id="rulesContainer" class="container mt-4 row card-container"></div>
    </div>
</div>

<div id="messageContainer" class="container mt-4"></div>

<!-- Update Modal -->
<div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateModalLabel">Update Rule</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="updateRuleInput" class="form-label">New Rule:</label>
                    <input type="text" class="form-control" id="updateRuleInput" required>
                </div>
                <div class="mb-3">
                    <label for="updateLinkInput" class="form-label">New Link:</label>
                    <input type="text" class="form-control" id="updateLinkInput" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" id="updateButton" class="btn btn-primary">Update</button>
            </div>
        </div>
    </div>
</div>


<script>
    // Check if the rule input is empty and disable/enable the button accordingly
    function checkInput() {
        var ruleInput = document.getElementById('ruleInput');
        var submitButton = document.getElementById('submitButton');

        if (ruleInput.value.trim() === '') {
            ruleInput.classList.add('border-danger');
            submitButton.disabled = true;
        } else {
            ruleInput.classList.remove('border-danger');
            submitButton.disabled = false;
        }
    }

    document.getElementById('ruleInput').addEventListener('input', checkInput);

    document.getElementById('submitButton').addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the form from submitting the traditional way

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
            if (response.ok) {
                showAlert('messageContainer', 'Rule has been added successfully', 'success');
                clearForm(); // Clear form fields after successful submission
                fetchRules();
            } else {
                throw new Error('Unexpected error');
            }
        })
        .catch(error => {
            showAlert('messageContainer', error.message, 'danger');
        });
    });

    // Function to clear form fields
    function clearForm() {
        document.getElementById('ruleInput').value = '';
        document.getElementById('linkInput').value = '';
        checkInput(); // Re-check the input after clearing the form
    }

document.getElementById('applyButton').addEventListener('click', function() {
    fetchRules(); // Fetch rules with the selected sort options
});

    // Function to fetch and display rules
    function fetchRules() {
    var sortBy = document.getElementById('sortBy').value;
    var sortDirection = document.getElementById('sortDirection').value;

    // Construct the URL using string concatenation
    var url = 'http://localhost:8080/rulesByUserId?userId=1&sortBy=' + encodeURIComponent(sortBy) + '&mode=' + encodeURIComponent(sortDirection);

    fetch(url)
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
    card.className = 'col-md-4'; // Ensure this class is used consistently
    card.setAttribute('data-rule-id', rule.ruleId); // Set ruleId as an attribute

    const cardContent = `
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title"></h5>
                <p class="card-text"><strong>Link:</strong> </p>
                <button class="btn btn-warning update-btn" data-bs-toggle="modal" data-bs-target="#updateModal">Update</button>
                <button class="btn btn-danger delete-btn">Delete</button>
            </div>
        </div>
    `;

    card.innerHTML = cardContent;
    card.querySelector('.card-title').textContent = rule.rule;
    card.querySelector('.card-text:nth-of-type(1)').append(document.createTextNode(rule.link));

    // Add event listeners for update and delete buttons
    const updateBtn = card.querySelector('.update-btn');
    updateBtn.setAttribute('data-rule-id', rule.ruleId);
    updateBtn.setAttribute('data-rule', rule.rule);
    updateBtn.setAttribute('data-link', rule.link);
    updateBtn.addEventListener('click', function() {
        document.getElementById('updateRuleInput').value = this.getAttribute('data-rule');
        document.getElementById('updateLinkInput').value = this.getAttribute('data-link');
        document.getElementById('updateButton').setAttribute('data-rule-id', this.getAttribute('data-rule-id'));
    });

    const deleteBtn = card.querySelector('.delete-btn');
    deleteBtn.setAttribute('data-rule-id', rule.ruleId);
    deleteBtn.addEventListener('click', function() {
        var ruleId = this.getAttribute('data-rule-id');
        deleteRule(ruleId);
    });

    return card;
}

    // Function to handle rule update
    document.getElementById('updateButton').addEventListener('click', function() {
        var ruleId = this.getAttribute('data-rule-id');
        var newRule = document.getElementById('updateRuleInput').value;
        var newLink = document.getElementById('updateLinkInput').value;

        var updateUrl = '/rule/' + encodeURIComponent(ruleId) + '?newRule=' + encodeURIComponent(newRule) + '&newLink=' + encodeURIComponent(newLink);

        fetch(updateUrl, {
            method: 'PUT',
            headers: {
                'accept': '*/*'
            }
        })
        .then(response => {
            if (response.ok) {
                showAlert('messageContainer', 'Rule has been updated successfully', 'success');
                fetchRules();
                document.querySelector('#updateModal .btn-close').click();
            } else {
                throw new Error('Unexpected error');
            }
        })
        .catch(error => {
            showAlert('messageContainer', error.message, 'danger');
        });
    });

    // Function to handle rule deletion
    function deleteRule(ruleId) {
        var deleteUrl = '/rule/' + encodeURIComponent(ruleId);

        fetch(deleteUrl, {
            method: 'DELETE',
            headers: {
                'accept': '*/*'
            }
        })
        .then(response => {
            if (response.ok) {
                showAlert('messageContainer', 'Rule has been deleted successfully', 'success');
                updateExercises();
                fetchRules();
            } else {
                throw new Error('Unexpected error');
            }
        })
        .catch(error => {
            showAlert('messageContainer', error.message, 'danger');
        });
    }

    // Function to update exercises
    function updateExercises() {
        fetch('http://localhost:8080/updateExercises?userId=1&mode=RULE', {
            method: 'PUT',
            headers: {
                'accept': '*/*'
            }
        })
        .then(response => {
            if (response.ok) {
                console.log('Exercises updated successfully');
            } else {
                throw new Error('Failed to update exercises');
            }
        })
        .catch(error => {
            console.error('Error updating exercises:', error);
        });
    }

    // Initial fetch of rules when the page loads
    fetchRules();


</script>
