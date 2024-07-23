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
                            <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search" autocomplete="off" data-topic-id="">
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
            </div>
            </form>
        </div>
    </div>
</div>
</div>


<script src="create_modal.js"></script>

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
                    const sentenceId = this.getAttribute('sentence-id');
                    const url = 'http://localhost:8080/api/sentence/getFullSentenceBySentenceId?sentenceId=' + sentenceId;

                    // Fetch the existing sentence data
                    fetch(url, {
                            method: 'GET',
                            headers: {
                                'Accept': '*/*',
                            }
                        })
                        .then(response => response.json())
                        .then(data => {
                            // Populate the form fields with the fetched data
                            document.getElementById('learningSentence').value = data.learningSentence;
                            document.getElementById('comment').value = data.comment;
                            document.getElementById('userLink').value = data.userLink;

                            // Populate topics
                            const topicIds = data.topics.map(topic => topic.topicId).join(',');
                            document.getElementById('topicIds').value = topicIds;
                            document.getElementById('topicInput').value = data.topics.map(topic => topic.topicName).join(', ');

                            // Clear and populate translations
                            const translationsContainer = document.getElementById('translations');
                            translationsContainer.innerHTML = '';
                            data.translations.forEach(translation => {
                                const translationDiv = document.createElement('div');
                                translationDiv.classList.add('mb-3');

                                // Create translation input
                                const translationInput = document.createElement('input');
                                translationInput.type = 'text';
                                translationInput.classList.add('form-control', 'mb-1');
                                translationInput.name = 'translations[]';
                                translationInput.value = translation.translation;
                                translationDiv.appendChild(translationInput);

                                // Create rules and links inputs
                                translation.rulesAndLinks.forEach(rule => {
                                    const ruleInput = document.createElement('input');
                                    ruleInput.type = 'text';
                                    ruleInput.classList.add('form-control', 'mb-1');
                                    ruleInput.value = rule.rule;
                                    translationDiv.appendChild(ruleInput);

                                    const linkInput = document.createElement('input');
                                    linkInput.type = 'text';
                                    linkInput.classList.add('form-control', 'mb-1');
                                    linkInput.value = rule.link;
                                    translationDiv.appendChild(linkInput);
                                });

                                translationsContainer.appendChild(translationDiv);
                            });

                            // Show the modal
                            const createSentenceModal = new bootstrap.Modal(document.getElementById('createSentenceModal'));
                            createSentenceModal.show();
                        })
                        .catch(error => console.error('Error fetching sentence data:', error));
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
</script>
