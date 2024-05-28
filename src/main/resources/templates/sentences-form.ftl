<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Sentence</h1>
    <form id="sentenceForm">
        <div class="mb-3">
            <label for="learningSentence" class="form-label">Learning Sentence:</label>
            <input type="text" class="form-control" id="learningSentence" name="learningSentence">
        </div>
        <div class="mb-3">
            <label for="comment" class="form-label">Comment:</label>
            <input type="text" class="form-control" id="comment" name="comment">
        </div>
        <div class="mb-3">
            <label for="userLink" class="form-label">User Link:</label>
            <input type="text" class="form-control" id="userLink" name="userLink">
        </div>

        <div id="topicContainer" class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <div class="custom-dropdown topic-container">
                <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search" data-topic-id="">
                <div id="dropdownMenu" class="dropdown-menu scroll-container" role="menu"></div>
            </div>
            <input type="hidden" id="topicIds" name="topicIds">
        </div>

        <div id="additionalTopics"></div>

        <button type="button" class="btn btn-secondary mb-3" onclick="addTopicContainer()">Add Another Topic</button>

        <div class="mb-3">
            <h3>Translations</h3>
            <div id="translations"></div>
            <button type="button" class="btn btn-secondary mb-3" onclick="addTranslationContainer()">Add Translation</button>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>

        <div id="messageContainer" class="container mt-4" style="display: none;">
            <#if errorMessage??>
            <div class="alert alert-danger">${errorMessage}</div>
            <#elseif responseData??>
            <div class="alert alert-danger">An unexpected error occurred.</div>
            <#else>
            <div class="alert alert-danger">No response data received.</div>
        </#if>
</div>
</form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', async function() {
        let allTopics = [];
        let allRules = [];

        // Fetch topics and rules after DOM content has loaded
        try {
            const topicsResponse = await fetch('/topics?userId=1');
            allTopics = await topicsResponse.json();

            const rulesResponse = await fetch('/rulesByUserId?userId=1');
            allRules = await rulesResponse.json();
        } catch (error) {
            console.error('Error fetching topics or rules:', error);
        }

        const filterTopics = (inputValue, dropdownMenu) => {
            dropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredTopics = allTopics.filter(topic => topic.topicName.toLowerCase().includes(inputValue));
                filteredTopics.forEach(topic => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = topic.topicName;
                    option.dataset.topicId = topic.topicId; // Correct assignment
                    dropdownMenu.appendChild(option);
                });
            }
        };

        const setupTopicInput = (topicInput, dropdownMenu) => {
            topicInput.addEventListener('input', function(event) {
                const inputValue = event.target.value.trim().toLowerCase();
                filterTopics(inputValue, dropdownMenu);
                dropdownMenu.style.display = 'block'; // Show the dropdown when typing
            });

            topicInput.addEventListener('blur', function() {
                setTimeout(() => {
                    dropdownMenu.style.display = 'none';
                    // Clear input if the value is not in the dropdown
                    const selectedTopic = Array.from(dropdownMenu.children).find(child => child.textContent === topicInput.value);
                    if (!selectedTopic) {
                        topicInput.value = '';
                        topicInput.dataset.topicId = '';
                    }
                }, 100);
            });

            topicInput.addEventListener('click', function() {
                if (dropdownMenu.innerHTML.trim() !== '') {
                    dropdownMenu.style.display = 'block';
                }
            });

            dropdownMenu.addEventListener('click', function(event) {
                const target = event.target;
                if (target.classList.contains('dropdown-item')) {
                    topicInput.value = target.textContent;
                    topicInput.dataset.topicId = target.dataset.topicId; // Correct assignment
                    dropdownMenu.style.display = 'none';
                }
            });

            topicInput.addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                }
            });
        };

        const initialTopicInput = document.getElementById('topicInput');
        const initialDropdownMenu = document.getElementById('dropdownMenu');
        setupTopicInput(initialTopicInput, initialDropdownMenu);

        const createTopicContainer = () => {
            const topicContainer = document.createElement('div');
            topicContainer.classList.add('topic-container', 'mb-3');

            const topicInput = document.createElement('input');
            topicInput.type = 'text';
            topicInput.classList.add('form-control', 'dropdown-input');
            topicInput.placeholder = 'Type to search';

            const dropdownMenu = document.createElement('div');
            dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

            setupTopicInput(topicInput, dropdownMenu);

            topicContainer.appendChild(topicInput);
            topicContainer.appendChild(dropdownMenu);

            const removeButton = document.createElement('button');
            removeButton.textContent = 'Remove Topic';
            removeButton.classList.add('btn', 'btn-danger', 'mt-2');
            removeButton.onclick = function() {
                topicContainer.remove();
            };
            topicContainer.appendChild(removeButton);

            return topicContainer;
        };

        window.addTopicContainer = function() {
            const additionalTopicsDiv = document.getElementById('additionalTopics');
            const topicContainer = createTopicContainer();
            additionalTopicsDiv.appendChild(topicContainer);
        };

        const filterRules = (inputValue, dropdownMenu) => {
            dropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredRules = allRules.filter(rule => rule.rule.toLowerCase().includes(inputValue));
                filteredRules.forEach(rule => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = rule.rule;
                    option.dataset.ruleId = rule.ruleId; // Set the correct rule ID here
                    dropdownMenu.appendChild(option);
                });
            }
        };

        const setupRuleInput = (ruleInput, ruleDropdownMenu) => {
            ruleInput.addEventListener('input', function(event) {
                const inputValue = event.target.value.trim().toLowerCase();
                filterRules(inputValue, ruleDropdownMenu);
                ruleDropdownMenu.style.display = 'block'; // Show the dropdown when typing
            });

            ruleInput.addEventListener('blur', function() {
                setTimeout(() => {
                    ruleDropdownMenu.style.display = 'none';
                    // Clear input if the value is not in the dropdown
                    const selectedRule = Array.from(ruleDropdownMenu.children).find(child => child.textContent === ruleInput.value);
                    if (!selectedRule) {
                        ruleInput.value = '';
                        ruleInput.dataset.ruleId = '';
                    }
                }, 100);
            });

            ruleInput.addEventListener('click', function() {
                if (ruleDropdownMenu.innerHTML.trim() !== '') {
                    ruleDropdownMenu.style.display = 'block';
                }
            });

            ruleDropdownMenu.addEventListener('click', function(event) {
                const target = event.target;
                if (target.classList.contains('dropdown-item')) {
                    ruleInput.value = target.textContent;
                    ruleInput.dataset.ruleId = target.dataset.ruleId; // Update the data-rule-id
                    ruleDropdownMenu.style.display = 'none';
                }
            });

            ruleInput.addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                }
            });

          // Store the current scroll position before focusing on the input field
    let scrollYBeforeFocus;
    ruleInput.addEventListener('focus', function(event) {
        scrollYBeforeFocus = window.scrollY;
    });

    // Restore the scroll position after a short delay
    ruleInput.addEventListener('blur', function(event) {
        setTimeout(() => {
            window.scrollTo(0, scrollYBeforeFocus);
        }, 100);
    });
        };

        const createRuleInput = (addRemoveButton = true) => {
            const ruleWrapper = document.createElement('div');
            ruleWrapper.classList.add('rule-wrapper', 'mb-3');

            const ruleInputWrapper = document.createElement('div');
            ruleInputWrapper.classList.add('d-flex', 'align-items-center');

            const ruleInput = document.createElement('input');
            ruleInput.type = 'text';
            ruleInput.classList.add('form-control', 'dropdown-input', 'mb-2', 'mr-2');
            ruleInput.placeholder = 'Type to search rule';

            const ruleDropdownMenu = document.createElement('div');
            ruleDropdownMenu.classList.add('dropdown-menu', 'scroll-container');

            setupRuleInput(ruleInput, ruleDropdownMenu);

            ruleInputWrapper.appendChild(ruleInput);

            if (addRemoveButton) {
                const removeButton = document.createElement('button');
                removeButton.textContent = 'Remove Rule';
                removeButton.classList.add('btn', 'btn-danger', 'ml-auto');
                removeButton.style.width = '150px';
                removeButton.onclick = function() {
                    ruleWrapper.remove();
                };
                ruleInputWrapper.appendChild(removeButton);
            }

            ruleWrapper.appendChild(ruleInputWrapper);
            ruleWrapper.appendChild(ruleDropdownMenu);

            return ruleWrapper;
        };

     const createTranslationContainer = async (initial = false) => {
    const translationContainer = document.createElement('div');
    translationContainer.classList.add('translation-container', 'mb-3');
    translationContainer.style.border = '1px solid #ccc';
    translationContainer.style.padding = '10px';
    translationContainer.style.borderRadius = '5px';

    const translationLabel = document.createElement('label');
    translationLabel.textContent = 'Translation:';
    translationLabel.classList.add('form-label');
    translationContainer.appendChild(translationLabel);

    const translationInput = document.createElement('input');
    translationInput.type = 'text';
    translationInput.classList.add('form-control', 'mb-3');
    translationInput.placeholder = 'Translation';
    translationContainer.appendChild(translationInput);

    const rulesLabel = document.createElement('label');
    rulesLabel.textContent = 'Rules:';
    rulesLabel.classList.add('form-label', 'mt-3');
    translationContainer.appendChild(rulesLabel);

    const rulesList = document.createElement('div');
    rulesList.classList.add('rules-list', 'mb-3');
    translationContainer.appendChild(rulesList);

    const addRuleButton = document.createElement('button');
    addRuleButton.type = 'button';
    addRuleButton.classList.add('btn', 'btn-primary', 'mb-3');
    addRuleButton.textContent = 'Add Rule';
    addRuleButton.onclick = function() {
        const ruleWrapper = createRuleInput();
        rulesList.appendChild(ruleWrapper);
    };
    translationContainer.appendChild(addRuleButton);

    if (!initial) {
        const removeTranslationButton = document.createElement('button');
        removeTranslationButton.type = 'button';
        removeTranslationButton.classList.add('btn', 'btn-danger', 'd-block', 'mt-2');
        removeTranslationButton.textContent = 'Remove Translation';
        removeTranslationButton.onclick = function() {
            translationContainer.remove();
        };
        translationContainer.appendChild(removeTranslationButton);
    }

    // Add one initial rule
    const initialRuleWrapper = createRuleInput();
    rulesList.appendChild(initialRuleWrapper);

    return translationContainer;
};





        window.addTranslationContainer = async function() {
            const translationsDiv = document.getElementById('translations');
            const translationContainer = await createTranslationContainer();
            translationsDiv.appendChild(translationContainer);
        };


       const validateForm = () => {
    let isValid = true;

    // Get the input fields
    const learningSentenceInput = document.getElementById('learningSentence');
    const commentInput = document.getElementById('comment');
    const userLinkInput = document.getElementById('userLink');
    const topicInputs = document.querySelectorAll('.dropdown-input');
    const translationContainers = document.querySelectorAll('.translation-container');

    // Validate individual fields
    [learningSentenceInput, commentInput, userLinkInput, ...topicInputs].forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid'); // Add class to make border red
            isValid = false;
        } else {
            input.classList.remove('is-invalid'); // Remove red border if valid
        }
    });

    // Validate translation containers
    translationContainers.forEach((container, index) => {
        const translationInput = container.querySelector('input[type="text"]');
        const ruleInputs = container.querySelectorAll('.rule-wrapper input[type="text"]');
        const allInputs = [translationInput, ...ruleInputs];

        allInputs.forEach(input => {
            if (!input.value.trim()) {
                input.classList.add('is-invalid'); // Add class to make border red
                isValid = false;
            } else {
                input.classList.remove('is-invalid'); // Remove red border if valid
            }
        });

        // Add red border to the translation field and the first rule field
        if (index === 0) {
            translationInput.classList.add('is-invalid');
            const firstRuleInput = container.querySelector('.rule-wrapper input[type="text"]');
            if (firstRuleInput) {
                firstRuleInput.classList.add('is-invalid');
            }
        }
    });

    return isValid;
};


        document.getElementById('sentenceForm').addEventListener('submit', async function(event) {
            event.preventDefault();

            const learningSentence = document.getElementById('learningSentence').value.trim();
            const mainTopicInput = document.querySelector('.topic-container input[type="text"]');

            if (learningSentence === '' || mainTopicInput === '') {
                // Show error message or handle empty fields as needed
                console.error('Learning sentence and topic are required.');
                return;
            }

            const topicsIds = [];

            // Collect the main topic ID
            const mainTopicId = parseInt(mainTopicInput.dataset.topicId);
            if (!isNaN(mainTopicId) && !topicsIds.includes(mainTopicId)) {
                topicsIds.push(mainTopicId);
            }

            // Collect additional topic IDs
            const additionalTopicInputs = document.querySelectorAll('#additionalTopics .topic-container input[type="text"]');
            additionalTopicInputs.forEach(input => {
                const topicId = parseInt(input.dataset.topicId);
                if (!isNaN(topicId) && !topicsIds.includes(topicId)) {
                    topicsIds.push(topicId);
                }
            });

            const translations = [];
            const translationContainers = document.querySelectorAll('.translation-container');
            translationContainers.forEach(container => {
                const translation = container.querySelector('input[type="text"]').value;
                const ruleIds = [];
                container.querySelectorAll('.rules-list .dropdown-input').forEach(ruleInput => {
                    const ruleId = parseInt(ruleInput.dataset.ruleId);
                    if (!isNaN(ruleId)) {
                        ruleIds.push(ruleId);
                    }
                });
                translations.push({
                    translation,
                    ruleIds
                });
            });

            const data = {
                userId: 1,
                learningSentence: document.getElementById('learningSentence').value,
                comment: document.getElementById('comment').value,
                userLink: document.getElementById('userLink').value,
                topicsIds: topicsIds,
                translations: translations
            };

            try {
                const response = await fetch('http://localhost:8080/vocabulary/add/sentence', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'accept': '*/*'
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    console.log('Form submitted successfully');
                    const messageContainer = document.getElementById('messageContainer');
                    messageContainer.innerHTML = '<div class="alert alert-success">Sentence was added successfully</div>';
                    messageContainer.style.display = 'block'; // Show the message container
                } else {
                    console.error('Error submitting form:', response.statusText);
                    messageContainer.style.display = 'block'; // Show the message container
                }
            } catch (error) {
                console.error('Error submitting form:', error);
                messageContainer.style.display = 'block'; // Show the message container
            }
        });

document.getElementById('sentenceForm').addEventListener('submit', function(event) {
            const requiredFields = ['learningSentence', 'topicInput']; // Add other required field IDs here
            const form = document.getElementById('sentenceForm');

            requiredFields.forEach(fieldId => {
                const field = document.getElementById(fieldId);
                if (field.value.trim() === '') {
                    field.classList.add('error'); // Apply red border
                } else {
                    field.classList.remove('error'); // Remove red border if field is not empty
                }
            });
        });

        // Add the initial translation container
        const initialTranslationContainer = await createTranslationContainer(true);
        document.getElementById('translations').appendChild(initialTranslationContainer);
    });
</script>
