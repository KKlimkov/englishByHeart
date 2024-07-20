<#include "common.ftl">

<div class="container mt-4">
    <h1>Sentences</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createSentenceModal">
        Create Sentence
    </button>
    <div class="mt-4">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Sentence ID</th>
                <th>Learning Sentence</th>
                <th>Comment</th>
                <th>User Link</th>
                <th>Translations</th>
                <th>Topics</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="sentencesTableBody">
            <!-- Data will be inserted here dynamically -->
            </tbody>
        </table>
    </div>
    <div id="messageContainerPage" class="container mt-4"></div>
</div>


<!-- Modal -->
<div class="modal fade" id="createSentenceModal" tabindex="-1" aria-labelledby="createSentenceModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="createSentenceModalLabel">Create Sentence</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="sentenceForm">
                    <div class="mb-3">
                        <label for="learningSentence" class="form-label">Learning Sentence:</label>
                        <input type="text" class="form-control" id="learningSentence" name="learningSentence" autocomplete="off">
                    </div>
                    <div class="mb-3">
                        <label for="comment" class="form-label">Comment:</label>
                        <input type="text" class="form-control" id="comment" name="comment" autocomplete="off">
                    </div>
                    <div class="mb-3">
                        <label for="userLink" class="form-label">User Link:</label>
                        <input type="text" class="form-control" id="userLink" name="userLink" autocomplete="off">
                    </div>

                    <div id="topicContainer" class="mb-3">
                        <label for="topicInput" class="form-label">Topic:</label>
                        <div class="custom-dropdown topic-container">
                            <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search"  autocomplete="off" data-topic-id="">
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

                    <div class="d-flex align-items-baseline">
                        <button type="submit" class="btn btn-primary">Submit</button>
                        <div id="messageContainer" class="mt-4" style="display: none;">
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
        </div>
    </div>
</div>

<script>

    async function fetchAndDisplaySentences() {
        try {
            const response = await fetch('http://localhost:8080/api/sentence/getFullSentencesByUserId?userId=1', {
                method: 'GET',
                headers: {
                    'Accept': '*/*'
                }
            });
            const sentences = await response.json();

            const tableBody = document.getElementById('sentencesTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            sentences.forEach(sentence => {
                const row = document.createElement('tr');

                const sentenceIdCell = document.createElement('td');
                sentenceIdCell.textContent = sentence.sentenceId;
                row.appendChild(sentenceIdCell);

                const learningSentenceCell = document.createElement('td');
                learningSentenceCell.textContent = sentence.learningSentence;
                row.appendChild(learningSentenceCell);

                const commentCell = document.createElement('td');
                commentCell.textContent = sentence.comment;
                row.appendChild(commentCell);

                const userLinkCell = document.createElement('td');
                userLinkCell.textContent = sentence.userLink;
                row.appendChild(userLinkCell);

                const translationsCell = document.createElement('td');
                const translationsList = document.createElement('ul');
                sentence.translations.forEach(translation => {
                    const translationItem = document.createElement('li');
                    var rulesAndLinks = [];
                    translation.rulesAndLinks.forEach(function(ruleAndLink) {
                        rulesAndLinks.push(ruleAndLink.rule);
                    });
                    translationItem.textContent = translation.translation + " (" + rulesAndLinks.join(', ') + ")";
                    translationsList.appendChild(translationItem);
                });
                translationsCell.appendChild(translationsList);
                row.appendChild(translationsCell);

                const topicsCell = document.createElement('td');
                const topicsList = document.createElement('ul');
                sentence.topics.forEach(topic => {
                    const topicItem = document.createElement('li');
                    topicItem.textContent = topic.topicName;
                    topicsList.appendChild(topicItem);
                });
                topicsCell.appendChild(topicsList);
                row.appendChild(topicsCell);

                const actionsCell = document.createElement('td');
                const updateButton = document.createElement('button');
                updateButton.textContent = 'Update';
                updateButton.classList.add('btn', 'btn-warning', 'm-1');
                updateButton.setAttribute('sentence-id', sentence.sentenceId);
                updateButton.addEventListener('click', function() {

                });

                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';
                deleteButton.classList.add('btn', 'btn-danger', 'm-1');
                deleteButton.setAttribute('sentence-id', sentence.sentenceId);
                deleteButton.addEventListener('click', function() {

                const sentenceId = this.getAttribute('sentence-id');
   const url = 'http://localhost:8080/api/sentence/' + sentenceId + '/user/1'; // Assuming user ID is 1

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Accept': '*/*',
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        // Optionally, handle successful deletion
        showAlert('messageContainerPage', 'Sentence deleted successfully!', 'Success');
        fetchAndDisplaySentences();
        // Optionally, remove the row or update the UI
    })
    .catch(error => {
        // Optionally, handle error
        console.error('There was a problem with the fetch operation:', error);
    });
                });

                actionsCell.appendChild(updateButton);
                actionsCell.appendChild(deleteButton);
                row.appendChild(actionsCell);

                tableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error fetching sentences:', error);
        }
    }

    // Call the function after the DOM content has loaded
    document.addEventListener('DOMContentLoaded', function() {
        fetchAndDisplaySentences();
    });



            async function updateExercises() {
        try {
                const exercisesResponse = await fetch('/updateExercises?userId=1', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                const exercises = await exercisesResponse.json();
                // Handle exercises as needed
            } catch (error) {
                console.error('Error fetching exercises:', error);
            }
        }

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


            document.getElementById('sentenceForm').addEventListener('submit', async function(event) {
            event.preventDefault();

            // Function to validate form fields
            const validateForm = () => {
                let isValid = true;
                const requiredFields = ['learningSentence']; // Only validate learningSentence for red border
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

            const learningSentence = document.getElementById('learningSentence').value.trim();
            const mainTopicInput = document.querySelector('.topic-container input[type="text"]');
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

                const messageContainer = document.getElementById('messageContainer');
                if (response.ok) {
                    console.log('Form submitted successfully');
                    messageContainer.innerHTML = '<div class="alert alert-success">Sentence was added successfully</div>';

                    // Close modal after adding sentence
    const addSentenceModal = bootstrap.Modal.getInstance(document.getElementById('createSentenceModal'));
    if (addSentenceModal) {
        addSentenceModal.hide(); // Close the modal
    } else {
        console.error('Modal instance not found');
    }

await fetchAndDisplaySentences();
                    updateExercises();
                } else {
                    console.error('Error submitting form:', response.statusText);
                    messageContainer.innerHTML = '<div class="alert alert-danger">Error submitting form</div>';
                }
                messageContainer.style.display = 'block'; // Show the message container
            } catch (error) {
                console.error('Error submitting form:', error);
                const messageContainer = document.getElementById('messageContainer');
                messageContainer.innerHTML = '<div class="alert alert-danger">Error submitting form</div>';
                messageContainer.style.display = 'block'; // Show the message container
            }
            document.getElementById('learningSentence').value = '';
        });

                // Add the initial translation container
                const initialTranslationContainer = await createTranslationContainer(true);
                document.getElementById('translations').appendChild(initialTranslationContainer);
            });
</script>
