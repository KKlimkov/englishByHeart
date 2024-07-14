<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Topic</h1>
    <div id="topicForm"> <!-- Changed from <form id="topicForm"> -->
        <div class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <input type="text" class="form-control" id="topicInput" name="topic" required>
        </div>
        <button type="button" id="submitButton" class="btn btn-primary">Create Topic</button> <!-- Changed from type="submit" -->
        <div id="topicsContainer" class="container mt-4 row"></div>
    </div>
</div>

<div id="messageContainer" class="container mt-4"></div>

<!-- Update Modal -->
<div class="modal fade" id="updateModal" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateModalLabel">Update Topic</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="updateTopicInput" class="form-label">New Topic Name:</label>
                    <input type="text" class="form-control" id="updateTopicInput" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" id="updateButton" class="btn btn-primary">Update</button>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('submitButton').addEventListener('click', function(event) {
     event.preventDefault(); // Prevent the form from submitting the traditional way

     var topicInput = document.getElementById('topicInput').value;
     var requestData = {
         userId: 1, // Assuming the user ID is fixed or you can obtain it dynamically
         topicName: topicInput
     };

     // Send AJAX request with JSON data
     fetch('/topics', {
         method: 'POST',
         headers: {
             'Content-Type': 'application/json'
         },
         body: JSON.stringify(requestData)
     })
     .then(response => {
         var messageContainer = document.getElementById('messageContainer');
         if (response.ok) {
             var message = document.createElement('div');
             message.classList.add('alert', 'alert-success');
             message.textContent = 'Topic has been added successfully';
             messageContainer.innerHTML = '';
             messageContainer.appendChild(message);
             clearForm(); // Clear form fields after successful submission
             fetchTopics();
         } else {
             throw new Error('Unexpected error');
         }
     })
     .catch(error => {
         var messageContainer = document.getElementById('messageContainer');
         var message = document.createElement('div');
         message.classList.add('alert', 'alert-danger');
         message.textContent = error.message;
         messageContainer.innerHTML = '';
         messageContainer.appendChild(message);
     });
 });

 // Function to clear form fields
 function clearForm() {
     document.getElementById('topicInput').value = '';
 }

 // Function to fetch and display topics
 function fetchTopics() {
     fetch('http://localhost:8080/topics?userId=1')
         .then(response => response.json())
         .then(data => {
             var topicsContainer = document.getElementById('topicsContainer');
             topicsContainer.innerHTML = ''; // Clear existing topics

             data.forEach(topic => {
                 var card = createTopicCard(topic);
                 topicsContainer.appendChild(card);
             });
         })
         .catch(error => {
             console.error('Error fetching topics:', error);
         });
 }

 // Function to create a topic card
 function createTopicCard(topic) {
     const card = document.createElement('div');
     card.className = 'col-md-4';
     card.setAttribute('data-topic-id', topic.topicId); // Set topicId as an attribute

     const cardContent = `
         <div class="card mb-4">
             <div class="card-body">
                 <h5 class="card-title"></h5>
                 <p class="card-text"><strong>User ID:</strong> </p>
                 <button class="btn btn-warning update-btn" data-bs-toggle="modal" data-bs-target="#updateModal">Update</button>
                 <button class="btn btn-danger delete-btn">Delete</button>
             </div>
         </div>
     `;

     card.innerHTML = cardContent;
     card.querySelector('.card-title').textContent = topic.topicName;
     card.querySelector('.card-text').append(document.createTextNode(topic.userId));

     // Add event listeners for update and delete buttons
     const updateBtn = card.querySelector('.update-btn');
     updateBtn.setAttribute('data-topic-id', topic.topicId);
     updateBtn.setAttribute('data-topic-name', topic.topicName);
     updateBtn.addEventListener('click', function() {
         document.getElementById('updateTopicInput').value = this.getAttribute('data-topic-name');
         document.getElementById('updateButton').setAttribute('data-topic-id', this.getAttribute('data-topic-id'));
     });

     const deleteBtn = card.querySelector('.delete-btn');
     deleteBtn.setAttribute('data-topic-id', topic.topicId);
     deleteBtn.addEventListener('click', function() {
         var topicId = this.getAttribute('data-topic-id');
         deleteTopic(topicId);
     });

     return card;
 }

 // Function to handle topic update
 document.getElementById('updateButton').addEventListener('click', function() {
     var topicId = this.getAttribute('data-topic-id');
     var newTopicName = document.getElementById('updateTopicInput').value;

     var updateUrl = '/topics/' + encodeURIComponent(topicId) + '?newTopicName=' + encodeURIComponent(newTopicName);

     fetch(updateUrl, {
         method: 'PUT',
         headers: {
             'accept': '*/*'
         }
     })
     .then(response => {
         if (response.ok) {
             var messageContainer = document.getElementById('messageContainer');
             var message = document.createElement('div');
             message.classList.add('alert', 'alert-success');
             message.textContent = 'Topic has been updated successfully';
             messageContainer.innerHTML = '';
             messageContainer.appendChild(message);
             fetchTopics();
             document.querySelector('#updateModal .btn-close').click();
         } else {
             throw new Error('Unexpected error');
         }
     })
     .catch(error => {
         var messageContainer = document.getElementById('messageContainer');
         var message = document.createElement('div');
         message.classList.add('alert', 'alert-danger');
         message.textContent = error.message;
         messageContainer.innerHTML = '';
         messageContainer.appendChild(message);
     });
 });

 // Function to handle topic deletion
 function deleteTopic(topicId) {
     var deleteUrl = '/topics/' + encodeURIComponent(topicId);

     fetch(deleteUrl, {
         method: 'DELETE',
         headers: {
             'accept': '*/*'
         }
     })
     .then(response => {
         if (response.ok) {
             var messageContainer = document.getElementById('messageContainer');
             var message = document.createElement('div');
             message.classList.add('alert', 'alert-success');
             message.textContent = 'Topic has been deleted successfully';
             messageContainer.innerHTML = '';
             messageContainer.appendChild(message);
             fetchTopics();
         } else {
             throw new Error('Unexpected error');
         }
     })
     .catch(error => {
         var messageContainer = document.getElementById('messageContainer');
         var message = document.createElement('div');
         message.classList.add('alert', 'alert-danger');
         message.textContent = error.message;
         messageContainer.innerHTML = '';
         messageContainer.appendChild(message);
     });
 }

 // Initial fetch of topics when the page loads
 fetchTopics();
</script>


