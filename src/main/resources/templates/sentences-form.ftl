<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Sentence</h1>
    <form action="/create-sentence" method="post">
        <div class="mb-3">
            <label for="learningSentence" class="form-label">Learning Sentence:</label>
            <input type="text" class="form-control" id="learningSentence" name="learningSentence" required>
        </div>
        <div class="mb-3">
            <label for="comment" class="form-label">Comment:</label>
            <input type="text" class="form-control" id="comment" name="comment">
        </div>
        <div class="mb-3">
            <label for="userLink" class="form-label">User Link:</label>
            <input type="text" class="form-control" id="userLink" name="userLink">
        </div>
        <!-- Add any other input fields as needed -->

        <div class="mb-3">
            <label for="topic" class="form-label">Topic:</label>
            <select class="form-select" id="topic" name="topic">
                <option value="">Select Topic</option>
                <#list topics! as topic>
                    <option value="${topic.topicId}">${topic.topicName}</option>
                </#list>
            </select>
        </div>

        <div id="additionalTopics"></div>

        <button type="button" class="btn btn-secondary mb-3" onclick="addTopicDropdown()">Add Another Topic</button>


        <div class="mb-3">
            <label for="rule" class="form-label">Rule:</label>
            <select class="form-select" id="rule" name="rule">
                <option value="">Select Rule</option>
                <#list rules! as rule>
                <option value="${rule.ruleId}">${rule.rule}</option>
            </#list>
            </select>
        </div>

        <div id="additionalRules"></div>
        <button type="button" class="btn btn-secondary mb-3" onclick="addAdditionalRuleDropdown()">Add Another Rule</button>

        <div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </div>

    </form>
</div>




<script>
    // Fetch topics data from the server
    fetch('/topics?userId=1')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch topics');
            }
            return response.json();
        })
        .then(topics => {
            const topicSelect = document.getElementById('topic');
            topics.forEach(topic => {
                const option = document.createElement('option');
                option.value = topic.topicId;
                option.textContent = topic.topicName;
                topicSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching topics:', error));
</script>

<script>
    function addTopicDropdown() {
        // Fetch topics data from the server
        fetch('/topics?userId=1')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch topics');
                }
                return response.json();
            })
            .then(topics => {
                const additionalTopicsDiv = document.getElementById('additionalTopics');

                // Create a container div for the dropdown and the button
                const containerDiv = document.createElement('div');
                containerDiv.classList.add('d-flex', 'align-items-center');

                // Create the dropdown
                const newTopicSelect = document.createElement('select');
                newTopicSelect.classList.add('form-select', 'mb-3');
                newTopicSelect.name = 'additionalTopics';
                newTopicSelect.innerHTML = '<option value="">Select Topic</option>';
                topics.forEach(topic => {
                    const option = document.createElement('option');
                    option.value = topic.topicId;
                    option.textContent = topic.topicName;
                    newTopicSelect.appendChild(option);
                });

                // Create the button to remove the added topic
                const removeButton = document.createElement('button');
                removeButton.textContent = 'Remove';
                removeButton.classList.add('btn', 'btn-danger', 'mb-3', 'ms-2'); // Add ms-2 class for margin to the left
                removeButton.onclick = function() {
                    additionalTopicsDiv.removeChild(containerDiv); // Remove the entire container div
                };

                // Append the dropdown and the button to the container div
                containerDiv.appendChild(newTopicSelect);
                containerDiv.appendChild(removeButton);

                // Append the container div to the additionalTopicsDiv
                additionalTopicsDiv.appendChild(containerDiv);
            })
            .catch(error => console.error('Error fetching topics:', error));
    }
</script>

<script>
    // Fetch rules data from the server
    fetch('/rulesByUserId?userId=1')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch rules');
            }
            return response.json();
        })
        .then(rules => {
            const ruleSelect = document.getElementById('rule');
            rules.forEach(rule => {
                const option = document.createElement('option');
                option.value = rule.ruleId;
                option.textContent = rule.rule;
                ruleSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching rules:', error));
</script>

<script>
    function addAdditionalRuleDropdown() {
        // Fetch rules data from the server
        fetch('/rulesByUserId?userId=1')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch rules');
                }
                return response.json();
            })
            .then(rules => {
                const additionalRulesDiv = document.getElementById('additionalRules');

                // Create a container div for the dropdown and the button
                const containerDiv = document.createElement('div');
                containerDiv.classList.add('d-flex', 'align-items-center', 'mb-3'); // Add mb-3 class for margin at the bottom

                // Create the dropdown
                const newRuleSelect = document.createElement('select');
                newRuleSelect.classList.add('form-select', 'me-2');
                newRuleSelect.name = 'additionalRules';
                newRuleSelect.innerHTML = '<option value="">Select Rule</option>';
                rules.forEach(rule => {
                    const option = document.createElement('option');
                    option.value = rule.ruleId;
                    option.textContent = rule.rule;
                    newRuleSelect.appendChild(option);
                });

                // Create the button to remove the added rule
                const removeButton = document.createElement('button');
                removeButton.textContent = 'Remove';
                removeButton.classList.add('btn', 'btn-danger');
                removeButton.onclick = function() {
                    additionalRulesDiv.removeChild(containerDiv); // Remove the entire container div
                };

                // Append the dropdown and the button to the container div
                containerDiv.appendChild(newRuleSelect);
                containerDiv.appendChild(removeButton);

                // Append the container div to the additionalRulesDiv
                additionalRulesDiv.appendChild(containerDiv);
            })
            .catch(error => console.error('Error fetching rules:', error));
    }
</script>