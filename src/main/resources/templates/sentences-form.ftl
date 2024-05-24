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

        <div id="topicContainer" class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <div class="custom-dropdown topic-container">
                <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search">
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

        <button type="button" class="btn btn-primary" onclick="submitForm(event)">Submit</button>

        <div id="messageContainer" class="container mt-4">
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
                dropdownMenu.appendChild(option);
            });
        }
    };

    const setupTopicInput = (topicInput, dropdownMenu) => {
        topicInput.addEventListener('input', function(event) {
            const inputValue = event.target.value.trim().toLowerCase();
            filterTopics(inputValue, dropdownMenu);


        });

        topicInput.addEventListener('focus', function() {
            if (dropdownMenu.innerHTML.trim() !== '') {
                dropdownMenu.style.display = 'block';
            }
        });

        topicInput.addEventListener('blur', function() {
            setTimeout(() => {
                const inputValue = topicInput.value.trim().toLowerCase();
                const matchingTopic = allTopics.find(topic => topic.topicName.toLowerCase() === inputValue);
                if (!matchingTopic) {
                    topicInput.value = '';
                }
                dropdownMenu.style.display = 'none';
            }, 100);
        });

        topicInput.addEventListener('click', function() {
            dropdownMenu.style.display = 'block';
        });

        dropdownMenu.addEventListener('click', function(event) {
            const target = event.target;
            if (target.classList.contains('dropdown-item')) {
                topicInput.value = target.textContent;
                dropdownMenu.style.display = 'none';
            }
        });

        // Prevent form submission when pressing Enter in the topic input field
        topicInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
            }
        });

         dropdownMenu.addEventListener('click', function(event) {
        const target = event.target;
        if (target.classList.contains('dropdown-item')) {
            topicInput.value = target.textContent;
            const topicId = target.dataset.topicId;
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'topicIds'; // This should match the name in your form
            hiddenInput.value = topicId;
            topicInput.parentNode.appendChild(hiddenInput); // Append to the parent node of the topic input
            dropdownMenu.style.display = 'none';
        }
    });
    };

    // Initial topic search field setup
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
            option.dataset.ruleId = rule.id; // Set the rule ID
            dropdownMenu.appendChild(option);
        });
    }
};


    const setupRuleInput = (ruleInput, ruleDropdownMenu) => {
    ruleInput.addEventListener('input', function(event) {
        const inputValue = event.target.value.trim().toLowerCase();
        filterRules(inputValue, ruleDropdownMenu);
        ruleDropdownMenu.style.display = 'block'; // Ensure the dropdown menu is displayed
    });

    ruleInput.addEventListener('blur', function() {
        setTimeout(() => {
            const inputValue = ruleInput.value.trim().toLowerCase();
            const matchingRule = allRules.find(rule => rule.rule.toLowerCase() === inputValue);
            if (!matchingRule) {
                ruleInput.value = '';
            }
            ruleDropdownMenu.style.display = 'none';
        }, 100);
    });

    ruleInput.addEventListener('click', function() {
        ruleDropdownMenu.style.display = 'block';
    });

    ruleDropdownMenu.addEventListener('click', function(event) {
        const target = event.target;
        if (target.classList.contains('dropdown-item')) {
            ruleInput.value = target.textContent;
            ruleInput.dataset.ruleId = target.dataset.ruleId; // Set the rule ID
            ruleDropdownMenu.style.display = 'none';
        }
    });

    // Prevent form submission when pressing Enter in the rule input field
    ruleInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
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
            removeButton.style.width = '150px'; // Adjust width here
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

        const translationInput = document.createElement('input');
        translationInput.type = 'text';
        translationInput.classList.add('form-control', 'mb-3');
        translationInput.placeholder = 'Translation';
        translationContainer.appendChild(translationInput);

        const rulesList = document.createElement('div');
        rulesList.classList.add('rules-list', 'mb-3');
        translationContainer.appendChild(rulesList);

        const addRuleButton = document.createElement('button');
        addRuleButton.textContent = 'Add Rule';
        addRuleButton.classList.add('btn', 'btn-primary', 'mb-3');
        addRuleButton.type = 'button';
        addRuleButton.onclick = function() {
            const ruleInput = createRuleInput();
            rulesList.appendChild(ruleInput);
        };
        translationContainer.appendChild(addRuleButton);

        // Create search type control (only if it doesn't exist)
        if (!rulesList.querySelector('.rule-wrapper')) {
            const ruleInput = createRuleInput(false);
            rulesList.appendChild(ruleInput);
        }

        // Prevent form submission when pressing Enter in the translation input field
        translationInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
            }
        });

        // Create a div to contain the Remove Translation button
        if (!initial) {
            const removeButtonContainer = document.createElement('div');
            removeButtonContainer.classList.add('mb-3');

            const removeButton = document.createElement('button');
            removeButton.textContent = 'Remove Translation';
            removeButton.classList.add('btn', 'btn-danger');
            removeButton.type = 'button';
            removeButton.onclick = function() {
                translationContainer.remove();
            };
            removeButtonContainer.appendChild(removeButton);

            translationContainer.appendChild(removeButtonContainer);
        }

        return translationContainer;
    };

    window.addTranslationContainer = async function() {
        const translationsDiv = document.getElementById('translations');
        const translationContainer = await createTranslationContainer();
        translationsDiv.appendChild(translationContainer);
    };

    // Create initial translation container without the Remove button
    const translationsDiv = document.getElementById('translations');
    const initialTranslationContainer = await createTranslationContainer(true);
    translationsDiv.appendChild(initialTranslationContainer);

const extractTopicIds = () => {
        const topicInputs = document.querySelectorAll('.topic-container input[type="text"]');
        const topicIds = [];
        topicInputs.forEach(input => {
            if (input.dataset.topicId) {
                topicIds.push(parseInt(input.dataset.topicId));  // Convert to integer
            }
        });
        return topicIds;
    };

     const extractTranslations = () => {
    const translations = [];
    const translationContainers = document.querySelectorAll('.translation-container');
    translationContainers.forEach(container => {
        const translationInput = container.querySelector('input[type="text"]');
        if (!translationInput) return;

        const translation = translationInput.value;

        const ruleInputs = container.querySelectorAll('.rule-wrapper input[type="text"]');
        const ruleIds = [];
        ruleInputs.forEach(input => {
            if (input.dataset.ruleId) {
                ruleIds.push(parseInt(input.dataset.ruleId));  // Convert to integer
            }
        });

        translations.push({
            translation,
            ruleIds
        });
    });
    return translations;
};

   window.submitForm = async function(event) {
    event.preventDefault();

    const form = document.getElementById('sentenceForm');
    const formData = new FormData(form);

    const topics = [];
    const topicInput = document.getElementById('topicInput');
    const topicId = topicInput.dataset.topicId;
    if (topicId) {
        topics.push(topicId);
    }

    const additionalTopicContainers = document.querySelectorAll('#additionalTopics .topic-container');
    additionalTopicContainers.forEach(container => {
        const input = container.querySelector('input');
        const id = input.dataset.topicId;
        if (id) {
            topics.push(id);
        }
    });

    const translations = [];
    const translationContainers = document.querySelectorAll('#translations .rule-wrapper');
    translationContainers.forEach(container => {
        const input = container.querySelector('input');
        const id = input.dataset.ruleId;
        if (id) {
            translations.push(id);
        }
    });

    const data = {
        learningSentence: formData.get('learningSentence'),
        comment: formData.get('comment'),
        userLink: formData.get('userLink'),
        topicIds: topics,
        translations: translations
    };

    try {
        const response = await fetch('/learning-sentence', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const responseData = await response.json();
        const messageContainer = document.getElementById('messageContainer');
        messageContainer.innerHTML = ''; // Clear previous content

        if (response.ok) {
            if (responseData.message) {
                const message = document.createElement('div');
                message.classList.add('alert', 'alert-success');
                message.textContent = responseData.message;
                messageContainer.appendChild(message);
            } else {
                console.error('Invalid response data:', responseData);
            }
        } else {
            throw new Error(responseData.errorMessage || 'An unexpected error occurred.');
        }

    } catch (error) {
        console.error('Error submitting form:', error);
        const messageContainer = document.getElementById('messageContainer');
        messageContainer.innerHTML = ''; // Clear previous content

        // Assign errorMessage to the FreeMarker model
        window.errorMessage = 'An unknown error occurred.';
        if (error && error.message) {
            window.errorMessage = error.message;
        }
    }
};


});
</script>