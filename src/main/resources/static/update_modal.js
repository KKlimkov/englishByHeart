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
    learningSentenceField.value = sentenceData.learningSentence;
    commentField.value = sentenceData.comment;
    userLinkField.value = sentenceData.userLink;

    // Continue with topics and translations...
}

//Helper functions to add topic and translation containers
function addTopicContainer(topicName) {
    if (topicName === undefined) topicName = '';
    const additionalTopicsDiv = document.getElementById('additionalTopics');
    const topicContainer = document.createElement('div');
    topicContainer.classList.add('mb-3');
    topicContainer.innerHTML = `
        <label for="topicInput" class="form-label">Topic:</label>
        <input type="text" class="form-control topic-input" autocomplete="off" value="${encodeURIComponent(topicName)}">
    `;
    additionalTopicsDiv.appendChild(topicContainer);
}

function addTranslationContainer(translation, rulesAndLinks) {
    if (translation === undefined) translation = '';
    if (rulesAndLinks === undefined) rulesAndLinks = [];
    const translationsDiv = document.getElementById('translations');
    const translationContainer = document.createElement('div');
    translationContainer.classList.add('translation-container');
    translationContainer.innerHTML = `
        <input type="text" class="form-control translation-input mb-2" placeholder="Translation" value="${encodeURIComponent(translation)}">
        <input type="text" class="form-control rulesAndLinks-input mb-2" placeholder="Rules and Links (comma separated)" value="${encodeURIComponent(rulesAndLinks.join(', '))}">
    `;
    translationsDiv.appendChild(translationContainer);
}

function showAlert(containerId, message, type) {
    const container = document.getElementById(containerId);
    container.innerHTML = '<div class="alert alert-' + type.toLowerCase() + '">' + encodeURIComponent(message) + '</div>';
    container.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function() {
    const updateForm = document.getElementById('updateForm');

    updateForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        console.log('Update form submitted');

        const formData = new FormData(updateForm);
        const exerciseData = {
            sentenceId: formData.get('sentenceId'),
            userId: formData.get('userId'),
            learningSentence: formData.get('learningSentence'),
            comment: formData.get('comment'),
            userLink: formData.get('userLink'),
            topics: getTopicsFromForm(),
            translations: getTranslationsFromForm()
        };

        try {
            console.log('Sending update request with data:', exerciseData);
            const response = await fetch('/api/sentence/' + encodeURIComponent(exerciseData.sentenceId), {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(exerciseData)
            });

            if (response.ok) {
                showAlert('messageContainerPage', 'Exercise updated successfully!', 'success');
                fetchAndDisplaySentences();
                console.log('Exercise updated successfully');

                // Hide the update modal
                const updateModalElement = document.getElementById('updateSentenceModal');
                const updateModal = bootstrap.Modal.getInstance(updateModalElement);
                updateModal.hide();
                console.log('Update modal hidden');
            } else {
                console.error('Failed to update exercise:', response.status);
                showAlert('messageContainer', 'Failed to update exercise.', 'danger');
            }
        } catch (error) {
            console.error('Error updating exercise:', error);
            showAlert('messageContainer', 'An unexpected error occurred.', 'danger');
        }
    });
});

function getTopicsFromForm() {
    const topics = [];
    const topicInputs = document.querySelectorAll('.topic-input');
    topicInputs.forEach(function(input) {
        if (input.value) {
            topics.push({ topicName: input.value });
        }
    });
    return topics;
}

function getTranslationsFromForm() {
    const translations = [];
    const translationContainers = document.querySelectorAll('.translation-container');
    translationContainers.forEach(function(container) {
        const translation = container.querySelector('.translation-input').value;
        const rulesAndLinks = container.querySelector('.rulesAndLinks-input').value.split(',').map(function(rule) {
            return rule.trim();
        });
        translations.push({ translation: translation, rulesAndLinks: rulesAndLinks.map(function(rule) { return { rule: rule }; }) });
    });
    return translations;
}
