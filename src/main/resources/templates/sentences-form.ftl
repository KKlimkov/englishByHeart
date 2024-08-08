<#include "common.ftl">

<div class="container mt-4">
    <h1>Sentences</h1>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createSentenceModal">
        Create Sentence
    </button>
    <div class="mt-4">
        <div class="mb-3">
            <label for="sortBy" class="form-label">Sort By:</label>
            <select id="sortBy" class="form-select">
                <option value="CREATE_DATE">Create Date</option>
                <option value="UPDATE_DATE">Update Date</option>
                <option value="RULE_NAME">Rule</option>
                <option value="TOPIC_NAME">Topic</option>
                <option value="LEARNING_SENTENCE">Sentence</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="sortDirection" class="form-label">Direction:</label>
            <select id="sortDirection" class="form-select">
                <option value="ASC">Ascending</option>
                <option value="DESC">Descending</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="searchWord" class="form-label">Search by word:</label>
            <input type="text" class="form-control" id="searchWord" name="searchWord" autocomplete="off">
        </div>
        <button type="button" id="applyButton" class="btn btn-primary">Apply</button>
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
    <div id="paginationControls" class="mt-4">
        <!-- Pagination buttons will be inserted here dynamically -->
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
                <form id="createSentenceForm">
                    <div class="mb-3">
                        <label for="createLearningSentence" class="form-label">Learning Sentence:</label>
                        <input type="text" class="form-control" id="createLearningSentence" name="learningSentence" autocomplete="off">
                    </div>
                    <div class="mb-3">
                        <label for="createComment" class="form-label">Comment:</label>
                        <input type="text" class="form-control" id="createComment" name="comment" autocomplete="off">
                    </div>
                    <div class="mb-3">
                        <label for="createUserLink" class="form-label">User Link:</label>
                        <input type="text" class="form-control" id="createUserLink" name="userLink" autocomplete="off">
                    </div>

                    <div id="createTopicContainer" class="mb-3">
                        <h5>Topics:</h5>
                        <div class="custom-dropdown topic-container">
                            <input type="text" class="form-control dropdown-input" id="createTopicInput" name="topicInput" placeholder="Type to search" autocomplete="off" data-topic-id="">
                            <div id="createDropdownMenu" class="dropdown-menu scroll-container" role="menu"></div>
                        </div>
                        <input type="hidden" id="createTopicIds" name="topicIds">
                    </div>

                    <div id="createAdditionalTopics"></div>

                    <button type="button" class="btn btn-secondary mb-3" onclick="addCreateTopicContainer()">Add Another Topic</button>

                    <div class="mb-3">
                        <h5>Translations:</h5>
                        <div id="createTranslations"></div>
                        <button type="button" class="btn btn-secondary mb-3" onclick="addCreateTranslationContainer()">Add Translation</button>
                    </div>

                    <div class="d-flex align-items-baseline">
                        <button type="submit" class="btn btn-primary">Submit</button>
                        <div id="createMessageContainer" class="mt-4" style="display: none;">
                            <#if errorMessage??>
                            <div class="alert alert-danger">${errorMessage}</div>
                            <#elseif responseData??>
                            <div class="alert alert-danger">An unexpected error occurred.</div>
                        </#if>
                    </div>
            </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Update Modal -->
<div class="modal fade" id="updateSentenceModal" tabindex="-1" aria-labelledby="updateSentenceModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateSentenceModalLabel">Update Sentence</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="updateSentenceForm">
                    <input type="hidden" id="updateSentenceId" name="sentenceId">
                    <input type="hidden" id="updateUserId" name="userId">
                    <div class="mb-3">
                        <label for="updateLearningSentence" class="form-label">Learning Sentence</label>
                        <input type="text" class="form-control" id="updateLearningSentence" name="learningSentence">
                    </div>
                    <div class="mb-3">
                        <label for="updateComment" class="form-label">Comment</label>
                        <input type="text" class="form-control" id="updateComment" name="comment">
                    </div>
                    <div class="mb-3">
                        <label for="updateUserLink" class="form-label">User Link</label>
                        <input type="text" class="form-control" id="updateUserLink" name="userLink">
                    </div>
                    <h5>Topics:</h5>
                    <div id="updateAdditionalTopics" class="mb-3"></div>
                    <button type="button" id="addUpdateTopicButton" class="btn btn-secondary mb-3">Add Another Topic</button>

                    <br><h5>Translations:</h5>
                    <div id="updateTranslations" class="mb-3"></div>
                    <button type="button" id="addUpdateTranslationButton" class="btn btn-secondary">Add Another Translation</button>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</div>

<script src="updateExercisesFunctions.js"></script>
<script src="create_modal.js"></script>
<script src="update_modal.js"></script>


<script>
    async function fetchAndDisplaySentences(sortBy, sortDirection, searchWord, page, size) {
       try {
           var url = 'http://localhost:8080/api/sentence/getFullSentencesByUserId?userId=1';
           if (sortBy) {
               url += '&sortBy=' + sortBy;
           }
           if (sortDirection) {
               url += '&mode=' + sortDirection;
           }
           if (page !== undefined) {
               url += '&page=' + page;
           }
           if (size !== undefined) {
               url += '&size=' + size;
           }
           if (searchWord) {
               url += '&searchWord=' + searchWord;
           }

           const response = await fetch(url, {
               method: 'GET',
               headers: {
                   'Accept': '*/*'
               }
           });

           if (!response.ok) {
               throw new Error('Network response was not ok');
           }

           const sentences = await response.json();

           const tableBody = document.getElementById('sentencesTableBody');
           tableBody.innerHTML = ''; // Clear any existing rows

           sentences.content.forEach(function(sentence) {
               const row = document.createElement('tr');

               // Create and append cells for each sentence property
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
               sentence.translations.forEach(function(translation) {
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
               sentence.topics.forEach(function(topic) {
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

               const deleteButton = document.createElement('button');
               deleteButton.textContent = 'Delete';
               deleteButton.classList.add('btn', 'btn-danger', 'm-1');
               deleteButton.setAttribute('sentence-id', sentence.sentenceId);
               deleteButton.addEventListener('click', function() {
                   const sentenceId = this.getAttribute('sentence-id');
                   const deleteUrl = 'http://localhost:8080/api/sentence/' + sentenceId + '/user/1'; // Assuming user ID is 1

                   fetch(deleteUrl, {
                       method: 'DELETE',
                       headers: {
                           'Accept': '*/*',
                       }
                   })
                   .then(function(response) {
                       if (!response.ok) {
                           throw new Error('Network response was not ok');
                       }
                       // Optionally, handle successful deletion
                       showAlert('messageContainerPage', 'Sentence deleted successfully!', 'Success');
                       fetchAndDisplaySentences(sortBy, sortDirection, searchWord, page, size);
                   })
                   .catch(function(error) {
                       // Optionally, handle error
                       console.error('There was a problem with the fetch operation:', error);
                   });
               });

               actionsCell.appendChild(updateButton);
               actionsCell.appendChild(deleteButton);
               row.appendChild(actionsCell);

               tableBody.appendChild(row);
           });

           addUpdateEventListeners();

           // Handle pagination
           const paginationContainer = document.getElementById('paginationControls');
           paginationContainer.innerHTML = ''; // Clear any existing buttons

           for (var i = 0; i < sentences.totalPages; i++) {
               const pageButton = document.createElement('button');
               pageButton.textContent = i + 1;
               pageButton.classList.add('btn', 'btn-secondary', 'm-1');
               pageButton.addEventListener('click', function() {
                   fetchAndDisplaySentences(sortBy, sortDirection, searchWord, this.textContent - 1, size);
               });
               paginationContainer.appendChild(pageButton);
           }
       } catch (error) {
           console.error('Error fetching sentences:', error);
       }
   }

   document.getElementById('applyButton').addEventListener('click', function() {
       var sortBy = document.getElementById('sortBy').value;
       var sortDirection = document.getElementById('sortDirection').value;
       var searchWord = document.getElementById('searchWord').value;
       fetchAndDisplaySentences(sortBy, sortDirection, searchWord, 0, 50);
   });

   function addUpdateEventListeners() {
       const updateButtons = document.querySelectorAll('.btn-warning');
       updateButtons.forEach(button => {
           button.addEventListener('click', async function() {
               console.log('Update button clicked');
               const sentenceId = this.getAttribute('sentence-id');
               const url = '/api/sentence/getFullSentenceBySentenceId?sentenceId=' + encodeURIComponent(sentenceId);

               try {
                   console.log('Fetching sentence data for ID:', sentenceId);
                   const response = await fetch(url, {
                       method: 'GET',
                       headers: {
                           'Accept': 'application/json'
                       }
                   });
                   if (!response.ok) {
                       throw new Error('Network response was not ok');
                   }
                   const sentenceData = await response.json();
                   console.log('Sentence data fetched:', sentenceData);
                   fillUpdateModal(sentenceData);

                   // Show the update modal
                   const updateModal = new bootstrap.Modal(document.getElementById('updateSentenceModal'));
                   updateModal.show();
                   console.log('Update modal displayed');
               } catch (error) {
                   console.error('Error fetching sentence data:', error);
               }
           });
       });
   }

   document.addEventListener('DOMContentLoaded', function() {
       fetchAndDisplaySentences('CREATE_DATE', 'ASC', '', 0, 50);
   });

</script>
