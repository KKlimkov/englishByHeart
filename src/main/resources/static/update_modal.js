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

const createUpdateTopicContainer = (withRemoveButton = true) => {
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

window.addUpdateTopicContainer = function(topicName = '') {
    const additionalTopicsDiv = document.getElementById('updateAdditionalTopics');
    const topicContainers = additionalTopicsDiv.getElementsByClassName('topic-container');
    const isFirst = topicContainers.length === 0;
    const topicContainer = createUpdateTopicContainer(!isFirst);
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

    const initialRuleWrapper = createRuleInput(false);
    rulesList.appendChild(initialRuleWrapper);

    return translationContainer;
};

window.addUpdateTranslationContainer = async function() {
    const translationsDiv = document.getElementById('updateTranslations');
    const translationContainer = await createTranslationContainer();
    translationsDiv.appendChild(translationContainer);
};

document.getElementById('addUpdateTranslationButton').addEventListener('click', function() {
    addUpdateTranslationContainer();
});


function fillUpdateModal(sentenceData) {
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
    const updateAdditionalTopicsDiv = document.getElementById('updateAdditionalTopics');
    updateAdditionalTopicsDiv.innerHTML = '';
    const updateTranslationsDiv = document.getElementById('updateTranslations');
    updateTranslationsDiv.innerHTML = '';

    // Add topics
    sentenceData.topics.forEach(topic => {
        addUpdateTopicContainer(topic.topicName);
    });

    // Add translations
    sentenceData.translations.forEach(translation => {
        const rulesAndLinks = translation.rulesAndLinks.map(ruleAndLink => ruleAndLink.rule);
        addUpdateTranslationContainer(translation.translation, rulesAndLinks);
    });
}

function addUpdateTopicContainer(topicName) {
    if (topicName === undefined) topicName = '';
    const additionalTopicsDiv = document.getElementById('updateAdditionalTopics');
    const topicContainer = document.createElement('div');
    topicContainer.classList.add('mb-3');
    topicContainer.innerHTML = `
        <label for="topicInput" class="form-label">Topic:</label>
        <input type="text" class="form-control topic-input" autocomplete="off" value="${decodeURIComponent(topicName)}">
    `;
    additionalTopicsDiv.appendChild(topicContainer);
}

function addUpdateTranslationContainer(translation, rulesAndLinks) {
    if (translation === undefined) translation = '';
    if (rulesAndLinks === undefined) rulesAndLinks = [];
    const translationsDiv = document.getElementById('updateTranslations');
    const translationContainer = document.createElement('div');
    translationContainer.classList.add('translation-container');
    translationContainer.innerHTML = `
        <input type="text" class="form-control translation-input mb-2" placeholder="Translation" value="${decodeURIComponent(translation)}">
        <input type="text" class="form-control rulesAndLinks-input mb-2" placeholder="Rules and Links (comma separated)" value="${decodeURIComponent(rulesAndLinks.join(', '))}">
    `;
    translationsDiv.appendChild(translationContainer);
}
