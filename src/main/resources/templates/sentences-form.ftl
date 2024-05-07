<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Sentence</h1>
    <form id="sentenceForm">
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
            <h3>Translations</h3>
            <div id="translations"></div>
            <button type="button" class="btn btn-secondary mb-3" onclick="addTranslation()">Add Translation</button>
        </div>

        <div>
            <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>
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
    async function addTranslation() {
    try {
        // Fetch rules data from the server
        const response = await fetch('/rulesByUserId?userId=1');
        if (!response.ok) {
            throw new Error('Failed to fetch rules');
        }
        const rules = await response.json();

        // Create translation container
        const translationContainer = await createTranslationContainer(rules);

        // Append the translation container to the translations section
        document.getElementById('translations').appendChild(translationContainer);
    } catch (error) {
        console.error('Error fetching rules:', error);
    }
}

async function createTranslationContainer(rules) {
    // Create a container div for the translation and rules
    const translationContainer = document.createElement('div');
    translationContainer.classList.add('translation-container', 'mb-3');

    // Add frame around the container
    translationContainer.style.border = '1px solid #ccc';
    translationContainer.style.padding = '10px';
    translationContainer.style.borderRadius = '5px';

    // Create input field for the translation
    const translationInput = document.createElement('input');
    translationInput.classList.add('form-control', 'mb-3');
    translationInput.type = 'text';
    translationInput.placeholder = 'Translation';
    translationContainer.appendChild(translationInput);

    // Create select element for rules
    const ruleSelect = document.createElement('select');
    ruleSelect.classList.add('form-select', 'mb-3'); // Add margin bottom
    ruleSelect.innerHTML = '<option value="">Select Rule</option>';
    rules.forEach(rule => {
        const option = document.createElement('option');
        option.value = rule.ruleId;
        option.textContent = rule.rule;
        ruleSelect.appendChild(option);
    });
    translationContainer.appendChild(ruleSelect);

    // Function to add another rule select dropdown
    function addRuleSelect() {
    // Create a container div for the rule select dropdown and remove button
    const ruleContainer = document.createElement('div');
    ruleContainer.classList.add('rule-container', 'd-flex', 'align-items-center', 'mb-3'); // Add flex classes
    ruleContainer.style.border = '1px solid #ccc';
    ruleContainer.style.padding = '10px';
    ruleContainer.style.borderRadius = '5px';

    // Create the new rule select dropdown
    const newRuleSelect = document.createElement('select');
    newRuleSelect.classList.add('form-select', 'flex-grow-1', 'me-2'); // Add flex-grow and margin class
    rules.forEach(rule => {
        const option = document.createElement('option');
        option.value = rule.ruleId;
        option.textContent = rule.rule;
        newRuleSelect.appendChild(option);
    });
    ruleContainer.appendChild(newRuleSelect);

    // Create a button to remove the new rule select dropdown
    const removeButton = document.createElement('button');
    removeButton.textContent = 'Remove Rule';
    removeButton.classList.add('btn', 'btn-danger'); // Add btn-danger class
    removeButton.style.marginLeft = 'auto'; // Move to the right
    removeButton.onclick = function() {
        translationContainer.removeChild(ruleContainer);
    };
    ruleContainer.appendChild(removeButton);

    // Insert the container into the translation container before the addButton
    translationContainer.insertBefore(ruleContainer, addButton);
}


    // Create a button to add another rule select dropdown
    const addButton = document.createElement('button');
    addButton.textContent = 'Add Rule';
    addButton.classList.add('btn', 'btn-primary', 'mb-10'); // Add margin to the right
    addButton.onclick = addRuleSelect;
    translationContainer.appendChild(addButton);

    // Create a button to remove the translation
    const removeButton = document.createElement('button');
    removeButton.textContent = 'Remove Translation'; // Change button text
    removeButton.classList.add('btn', 'btn-danger'); // Add margin to the left
    removeButton.onclick = function() {
        translationContainer.parentNode.removeChild(translationContainer); // Remove the entire container div
    };
    translationContainer.appendChild(removeButton);

    // Return the translation container
    return translationContainer;
}
</script>


