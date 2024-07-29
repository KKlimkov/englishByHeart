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
                } else {
                    // Update data-topic-id with the selected topic's ID
                    topicInput.dataset.topicId = selectedTopic.dataset.topicId;
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

        // Prevent drag-and-drop
            topicInput.addEventListener('dragover', function(event) {
                event.preventDefault();
            });

            topicInput.addEventListener('drop', function(event) {
                event.preventDefault();
            });
    };

    const initialTopicInput = document.getElementById('createTopicInput');
    const initialDropdownMenu = document.getElementById('createDropdownMenu');
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

    window.addCreateTopicContainer = function() {
        const additionalTopicsDiv = document.getElementById('createAdditionalTopics');
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

         ruleInput.addEventListener('dragover', function(event) {
                        event.preventDefault();
                    });

                    ruleInput.addEventListener('drop', function(event) {
                        event.preventDefault();
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
            const ruleWrapper = createRuleInput(true);
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
        const initialRuleWrapper = createRuleInput(false);
        rulesList.appendChild(initialRuleWrapper);

        return translationContainer;
    };

    window.addCreateTranslationContainer = async function() {
        const translationsDiv = document.getElementById('createTranslations');
        const translationContainer = await createTranslationContainer();
        translationsDiv.appendChild(translationContainer);
    };

    const resetForm = () => {
            // Clear main input fields
            document.getElementById('createLearningSentence').value = '';
            document.getElementById('createComment').value = '';
            document.getElementById('createUserLink').value = '';

            // Clear topics
            document.getElementById('createTopicInput').value = '';
            document.getElementById('createTopicInput').dataset.topicId = '';
            document.getElementById('createDropdownMenu').innerHTML = '';

            // Clear additional topics
            document.getElementById('createAdditionalTopics').innerHTML = '';

            // Clear translations
            document.getElementById('createTranslations').innerHTML = '';
            addCreateTranslationContainer(); // Add the initial translation container
        };

        // Ensure you include jQuery if not already included
        $(document).ready(function() {
            $('#createSentenceModal').on('show.bs.modal', function() {
                resetForm(); // Call the reset form function when modal is shown
            });
        });

    const validateForm = () => {
        let isValid = true;

        // Get the input fields
        const learningSentenceInput = document.getElementById('createLearningSentence');
        const commentInput = document.getElementById('createComment');
        const userLinkInput = document.getElementById('createUserLink');
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
        translationContainers.forEach(container => {
            const translationInput = container.querySelector('input[type="text"]');
            const ruleInputs = container.querySelectorAll('.rule-wrapper input[type="text"]');

            if (!translationInput.value.trim()) {
                translationInput.classList.add('is-invalid'); // Add class to make border red
                isValid = false;
            } else {
                translationInput.classList.remove('is-invalid'); // Remove red border if valid
            }

            ruleInputs.forEach(input => {
                if (!input.value.trim()) {
                    input.classList.add('is-invalid'); // Add class to make border red
                    isValid = false;
                } else {
                    input.classList.remove('is-invalid'); // Remove red border if valid
                }
            });
        });

        return isValid;
    };


    document.getElementById('createSentenceForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        // Function to validate form fields
        const validateForm = () => {
            let isValid = true;
            const requiredFields = ['createLearningSentence']; // Only validate learningSentence for red border
            const mainTopicInput = document.querySelector('.topic-container input[type="text"]');
            const translationContainers = document.querySelectorAll('.translation-container');

            requiredFields.forEach(fieldId => {
                const field = document.getElementById(fieldId);
                if (field && field.value.trim() === '') {
                    field.classList.add('is-invalid'); // Apply red border
                    isValid = false;
                } else if (field) {
                    field.classList.remove('is-invalid'); // Remove red border if field is not empty
                }
            });

            if (mainTopicInput && mainTopicInput.value.trim() === '') {
                mainTopicInput.classList.add('is-invalid'); // Apply red border
                isValid = false;
            } else if (mainTopicInput) {
                mainTopicInput.classList.remove('is-invalid'); // Remove red border if field is not empty
            }

            translationContainers.forEach(container => {
                const translationInput = container.querySelector('input[type="text"]');
                const ruleInputs = container.querySelectorAll('.rule-wrapper input[type="text"]');

                if (translationInput && translationInput.value.trim() === '') {
                    translationInput.classList.add('is-invalid'); // Apply red border
                    isValid = false;
                } else if (translationInput) {
                    translationInput.classList.remove('is-invalid'); // Remove red border if field is not empty
                }

                ruleInputs.forEach(input => {
                    if (input && input.value.trim() === '') {
                        input.classList.add('is-invalid'); // Apply red border
                        isValid = false;
                    } else if (input) {
                        input.classList.remove('is-invalid'); // Remove red border if field is not empty
                    }
                });
            });

            return isValid;
        };

        if (!validateForm()) {
            console.error('Form validation failed.');
            return;
        }

        const learningSentence = document.getElementById('createLearningSentence').value.trim();
        const mainTopicInput = document.querySelector('.topic-container input[type="text"]');
        const topicsIds = [];

        // Collect the main topic ID
        const mainTopicId = parseInt(mainTopicInput.dataset.topicId);
        if (!isNaN(mainTopicId) && !topicsIds.includes(mainTopicId)) {
            topicsIds.push(mainTopicId);
        }

        // Collect additional topic IDs
        const additionalTopicInputs = document.querySelectorAll('#createAdditionalTopics .topic-container input[type="text"]');
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
            learningSentence: document.getElementById('createLearningSentence').value,
            comment: document.getElementById('createComment').value,
            userLink: document.getElementById('createUserLink').value,
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

            const messageContainer = document.getElementById('createMessageContainer');
            if (response.ok) {
                console.log('Form submitted successfully');
                    showAlert('messageContainerPage', 'Sentence was added successfully', 'Success');
                // Close modal after adding sentence using jQuery
                    $('#createSentenceModal').modal('hide');
                    $('.modal-backdrop').remove();
                await fetchAndDisplaySentences();
                updateExercises();

            } else {
                console.error('Error submitting form:', response.statusText);
                messageContainer.innerHTML = '<div class="alert alert-danger">Error submitting form</div>';
            }
            messageContainer.style.display = 'block'; // Show the message container
        } catch (error) {
            console.error('Error submitting form:', error);
            const messageContainer = document.getElementById('createMessageContainer');
            messageContainer.innerHTML = '<div class="alert alert-danger">Error submitting form</div>';
            messageContainer.style.display = 'block'; // Show the message container
        }
        document.getElementById('createLearningSentence').value = '';
    });

    // Add the initial translation container
    const initialTranslationContainer = await createTranslationContainer(true);

    const modalElement = document.getElementById('createSentenceModal');
    if (modalElement) {
    document.getElementById('createTranslations').appendChild(initialTranslationContainer);
    }

});