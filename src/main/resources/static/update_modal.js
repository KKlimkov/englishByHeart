let allTopics = [];

// Fetch topics after DOM content has loaded
async function fetchTopicsAndRules() {
    try {
        const topicsResponse = await fetch('/topics?userId=1');
        allTopics = await topicsResponse.json();
    } catch (error) {
        console.error('Error fetching topics:', error);
    }
}

fetchTopicsAndRules();

const filterTopics = (inputValue, dropdownMenu) => {
    dropdownMenu.innerHTML = '';
    if (inputValue.length >= 2) {
        const filteredTopics = allTopics.filter(topic => topic.topicName.toLowerCase().includes(inputValue));
        filteredTopics.forEach(topic => {
            const option = document.createElement('a');
            option.classList.add('dropdown-item');
            option.href = '#';
            option.textContent = topic.topicName;
            option.dataset.topicId = topic.topicId;
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
            topicInput.dataset.topicId = target.dataset.topicId;
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

const createUpdateTopicContainer = (withRemoveButton = true, topicId = '') => {
    const topicContainer = document.createElement('div');
    topicContainer.classList.add('topic-container', 'mb-3');

    const topicInput = document.createElement('input');
    topicInput.type = 'text';
    topicInput.classList.add('form-control', 'dropdown-input');
    topicInput.placeholder = 'Type to search';
    if (topicId) {
        topicInput.dataset.topicId = topicId;
    }

    const dropdownMenu = document.createElement('div');
    dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

    setupTopicInput(topicInput, dropdownMenu);

    topicContainer.appendChild(topicInput);
    topicContainer.appendChild(dropdownMenu);

    if (withRemoveButton) {
        const removeButton = document.createElement('button');
        removeButton.textContent = 'Remove Topic';
        removeButton.classList.add('btn', 'btn-danger', 'mt-2');
        removeButton.onclick = function() {
            topicContainer.remove();
        };
        topicContainer.appendChild(removeButton);
    }

    return topicContainer;
};

window.addUpdateTopicContainer = function(topicName = '', topicId = '') {
    const additionalTopicsDiv = document.getElementById('updateAdditionalTopics');
    const topicContainers = additionalTopicsDiv.getElementsByClassName('topic-container');
    const isFirst = topicContainers.length === 0;
    const topicContainer = createUpdateTopicContainer(!isFirst, topicId);
    const topicInput = topicContainer.querySelector('.dropdown-input');
    topicInput.value = decodeURIComponent(topicName);
    additionalTopicsDiv.appendChild(topicContainer);
};

document.getElementById('addUpdateTopicButton').addEventListener('click', function() {
    addUpdateTopicContainer();
});


let allRules = [];

// Fetch rules after DOM content has loaded
async function fetchRules() {
    try {
        const rulesResponse = await fetch('/rulesByUserId?userId=1');
        allRules = await rulesResponse.json();
        console.log('Fetched rules:', allRules); // Add thi
    } catch (error) {
        console.error('Error fetching rules:', error);
    }
}

fetchRules();

const filterRules = (inputValue, dropdownMenu) => {
    dropdownMenu.innerHTML = '';
    if (inputValue.length >= 2) {
        const filteredRules = allRules.filter(rule => rule.rule.toLowerCase().includes(inputValue));
        filteredRules.forEach(rule => {
            const option = document.createElement('a');
            option.classList.add('dropdown-item');
            option.href = '#';
            option.textContent = rule.rule;
            option.dataset.ruleId = rule.ruleId;
            dropdownMenu.appendChild(option);
        });
    }
};

const setupRuleInput = (ruleInput, ruleDropdownMenu) => {
    ruleInput.addEventListener('input', function(event) {
        const inputValue = event.target.value.trim().toLowerCase();
        filterRules(inputValue, ruleDropdownMenu);
        ruleDropdownMenu.style.display = 'block';
    });

    ruleInput.addEventListener('blur', function() {
        setTimeout(() => {
            ruleDropdownMenu.style.display = 'none';
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
            ruleInput.dataset.ruleId = target.dataset.ruleId;
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

    let scrollYBeforeFocus;
    ruleInput.addEventListener('focus', function(event) {
        scrollYBeforeFocus = window.scrollY;
    });

    ruleInput.addEventListener('blur', function(event) {
        setTimeout(() => {
            window.scrollTo(0, scrollYBeforeFocus);
        }, 100);
    });
};

// Function to create rule input
const createRuleInput = (addRemoveButton = true, ruleId = '') => {
    const ruleWrapper = document.createElement('div');
    ruleWrapper.classList.add('rule-wrapper', 'mb-3');

    const ruleInput = document.createElement('input');
    ruleInput.type = 'text';
    ruleInput.classList.add('form-control', 'dropdown-input');
    ruleInput.placeholder = 'Type to search';
    if (ruleId) {
        ruleInput.dataset.ruleId = ruleId;
    }

    const ruleDropdownMenu = document.createElement('div');
    ruleDropdownMenu.classList.add('dropdown-menu', 'scroll-container');

    setupRuleInput(ruleInput, ruleDropdownMenu);

    ruleWrapper.appendChild(ruleInput);
    ruleWrapper.appendChild(ruleDropdownMenu);

    if (addRemoveButton) {
        const removeButton = document.createElement('button');
        removeButton.textContent = 'Remove Rule';
        removeButton.classList.add('btn', 'btn-danger', 'mt-2');
        removeButton.onclick = function() {
            ruleWrapper.remove();
        };
        ruleWrapper.appendChild(removeButton);
    }

    return ruleWrapper;
};

async function createTranslationContainer(translation = '', rulesAndLinks = [], initial = false) {
    console.log('Creating translation container with:', { translation, rulesAndLinks });

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
    translationInput.value = decodeURIComponent(translation);
    translationContainer.appendChild(translationInput);

    const rulesLabel = document.createElement('label');
    rulesLabel.textContent = 'Rules:';
    rulesLabel.classList.add('form-label', 'mt-3');
    translationContainer.appendChild(rulesLabel);

    const rulesList = document.createElement('div');
    rulesList.classList.add('rules-list');
    translationContainer.appendChild(rulesList);

    // Add initial rules
    if (rulesAndLinks.length > 0) {
        console.log('Adding initial rules:', rulesAndLinks);

        for (const { ruleId } of rulesAndLinks) {
            if (ruleId) {
                console.log('Fetching rule with ID:', ruleId);
                const ruleData = await fetchRuleById(ruleId);
                if (ruleData) {
                    console.log('Fetched rule data:', ruleData);
                    const ruleWrapper = createRuleInput(false, ruleId);
                    const ruleInput = ruleWrapper.querySelector('.dropdown-input');
                    ruleInput.value = ruleData.rule;
                    rulesList.appendChild(ruleWrapper);
                } else {
                    console.error('No data found for rule ID:', ruleId);
                }
            } else {
                console.error('Undefined ruleId:', ruleId);
            }
        }
    } else {
        console.log('No rules to add.');
    }

    const addRuleButton = document.createElement('button');
    addRuleButton.classList.add('btn', 'btn-secondary', 'mt-3');
    addRuleButton.type = 'button';
    addRuleButton.textContent = 'Add Rule';
    addRuleButton.onclick = () => {
        const ruleWrapper = createRuleInput();
        rulesList.appendChild(ruleWrapper);
    };
    translationContainer.appendChild(addRuleButton);

    if (!initial) {
        const removeTranslationButton = document.createElement('button');
        removeTranslationButton.classList.add('btn', 'btn-danger', 'mt-3');
        removeTranslationButton.type = 'button';
        removeTranslationButton.textContent = 'Remove Translation';
        removeTranslationButton.onclick = () => {
            translationContainer.remove();
        };
        translationContainer.appendChild(removeTranslationButton);
    }

    return translationContainer;
}


// Fetch rule by ID
async function fetchRuleById(ruleId) {
    if (!ruleId) {
        console.error('Invalid ruleId:', ruleId);
        return null;
    }
    try {
        const response = await fetch('/getRuleById/' + ruleId);
        const ruleData = await response.json();
        console.log('Fetched rule by ID:', ruleData);
        return ruleData;
    } catch (error) {
        console.error('Error fetching rule by ID:', error);
        return null;
    }
}

window.addUpdateTranslationContainer = async function(translation = '', rulesAndLinks = [], initial = false) {
    const additionalTranslationsDiv = document.getElementById('updateTranslations');
    if (!additionalTranslationsDiv) {
        console.error('Element with id "updateTranslations" not found.');
        return;
    }
    const translationContainer = await createTranslationContainer(translation, rulesAndLinks, initial);
    additionalTranslationsDiv.appendChild(translationContainer);
};

document.getElementById('addUpdateTranslationButton').addEventListener('click', async function() {
    await addUpdateTranslationContainer();
});

function fillUpdateModal(sentenceData) {
    const updateAdditionalTopicsDiv = document.getElementById('updateAdditionalTopics');
    const updateTranslationsDiv = document.getElementById('updateTranslations');
    if (!updateTranslationsDiv) {
        console.error('Element with id "updateTranslations" not found.');
        return;
    }

    console.log('Filling update modal with data:', sentenceData);

    const sentenceIdField = document.getElementById('updateSentenceId');
    const userIdField = document.getElementById('updateUserId');
    const learningSentenceField = document.getElementById('updateLearningSentence');
    const commentField = document.getElementById('updateComment');
    const userLinkField = document.getElementById('updateUserLink');

    if (!sentenceIdField || !userIdField || !learningSentenceField || !commentField || !userLinkField) {
        console.error('One or more form fields are missing in the update modal.');
        return;
    }

    sentenceIdField.value = sentenceData.sentenceId;
    userIdField.value = sentenceData.userId;
    learningSentenceField.value = decodeURIComponent(sentenceData.learningSentence);
    commentField.value = decodeURIComponent(sentenceData.comment);
    userLinkField.value = decodeURIComponent(sentenceData.userLink);

    // Clear existing topics and translations
    updateAdditionalTopicsDiv.innerHTML = '';
    updateTranslationsDiv.innerHTML = '';

    // Add topics
    sentenceData.topics.forEach(topic => {
        addUpdateTopicContainer(topic.topicName, topic.topicId);
    });

    // Add translations
    sentenceData.translations.forEach(async (translation) => {
        console.log('Processing translation:', translation);
        const rulesAndLinks = translation.rulesAndLinks.map(ruleAndLink => {
            console.log('Rule and Link:', ruleAndLink);
            return {
                ruleId: ruleAndLink.ruleId
            };
        });
        await addUpdateTranslationContainer(translation.translation, rulesAndLinks, false);
    });
}


document.getElementById('updateSentenceForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent default form submission

    // Gather form data
    const sentenceId = document.getElementById('updateSentenceId').value;
    const userId = document.getElementById('updateUserId').value;
    const learningSentence = document.getElementById('updateLearningSentence').value;
    const comment = document.getElementById('updateComment').value;
    const userLink = document.getElementById('updateUserLink').value;

    // Gather topic IDs
    const topicContainers = document.querySelectorAll('#updateAdditionalTopics .topic-container .dropdown-input');
    const topicsIds = Array.from(topicContainers).map(container => parseInt(container.dataset.topicId)).filter(id => !isNaN(id));

    // Gather translations and their rules
    const translationContainers = document.querySelectorAll('#updateTranslations .translation-container');
    const translations = Array.from(translationContainers).map(container => {
        const translationInput = container.querySelector('input[type="text"]');
        const rulesList = container.querySelectorAll('.rules-list .dropdown-input');
        const ruleIds = Array.from(rulesList).map(ruleInput => parseInt(ruleInput.dataset.ruleId)).filter(id => !isNaN(id));
        return {
            translation: translationInput.value,
            ruleIds: ruleIds
        };
    });

    // Create the data object to send in the PUT request
    const data = {
        userId: parseInt(userId),
        learningSentence: learningSentence,
        comment: comment,
        userLink: userLink,
        topicsIds: topicsIds,
        translations: translations
    };

    // Send the PUT request
    try {
        const response = await fetch('http://localhost:8080/vocabulary/update/sentence/'+sentenceId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'accept': '*/*'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Handle success (e.g., close modal, show success message, update UI)
        alert('Sentence updated successfully!');
        $('#updateSentenceModal').modal('hide');
        await fetchAndDisplaySentences();
        updateExercises();
        // Optionally, refresh data on the page or redirect to another page
    } catch (error) {
        console.error('Error updating sentence:', error);
        alert('An error occurred while updating the sentence.');
    }
});
