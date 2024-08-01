<#include "common.ftl">

<div class="container mt-4">
    <h1>Exercises</h1>
    <form id="exerciseForm">
        <button type="button" class="btn btn-primary" id="continueExerciseBtn">Continue current exercise</button>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addExerciseModal">Add new exercise</button>
    </form>
    <div id="exercisesContainer" class="mt-4">
        <h2>Exercises List</h2>
        <div class="row" id="exercisesList">
            <!-- Placeholder for exercises -->
        </div>
    </div>
</div>

<#-- Initialize topicIds and ruleIds as empty lists if they are not defined -->
<#assign topicIds = (topicIds![])?sequence>
<#assign ruleIds = (ruleIds![])?sequence>

<div class="modal fade" id="addExerciseModal" tabindex="-1" aria-labelledby="addExerciseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addExerciseModalLabel">Add New Exercise</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    <label for="name">Name:</label>
                    <input type="text" class="form-control" id="exerciseName" name="name" autocomplete="off">
                    <label for="topic">Choose topics:</label>
                    <div id="topicsContainer">
                        <div class="input-group mb-2">
                            <input type="text" class="form-control dropdown-input" placeholder="Type to search" data-topic-id=""  autocomplete="off">
                            <div class="input-group-append">
                                <button class="btn btn-danger" type="button" onclick="removeTopic(this)">Remove Topic</button>
                            </div>
                            <div class="dropdown-menu scroll-container"></div>
                        </div>
                    </div>
                    <button type="button" class="btn btn-secondary" onclick="addTopic()">Add Another Topic</button>
                </div>
                <div class="mt-4">
                    <label for="rules">Choose rules:</label>
                    <div id="rulesContainer">
                        <div class="input-group mb-2">
                            <input type="text" class="form-control dropdown-input" placeholder="Type to search rule" data-rule-id=""  autocomplete="off">
                            <div class="input-group-append">
                                <button class="btn btn-danger" type="button" onclick="removeRule(this)">Remove Rule</button>
                            </div>
                            <div class="dropdown-menu scroll-container"></div>
                        </div>
                    </div>
                    <button type="button" class="btn btn-secondary" onclick="addRule()">Add Rule</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="addExerciseBtn">Add</button>
            </div>
            <div id="messageContainer" class="mt-4" style="display: none;">
                <div id="alertMessage" class="alert alert-danger" role="alert"></div>
            </div>
        </div>
    </div>
</div>

<!-- Update Exercise Modal -->
<div class="modal fade" id="updateExerciseModal" tabindex="-1" aria-labelledby="updateExerciseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateExerciseModalLabel">Update Exercise</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    <label for="updateExerciseName">Name:</label>
                    <input type="text" class="form-control" id="updateExerciseName" name="name" autocomplete="off">
                    <label for="updateTopicsContainer">Choose topics:</label>
                    <div id="updateTopicsContainer"></div>
                    <button type="button" class="btn btn-secondary" onclick="addUpdateTopic()">Add Another Topic</button>
                </div>
                <div class="mt-4">
                    <label for="updateRulesContainer">Choose rules:</label>
                    <div id="updateRulesContainer"></div>
                    <button type="button" class="btn btn-secondary" onclick="addUpdateRule()">Add Rule</button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="updateExerciseBtn">Update</button>
            </div>
            <div id="updateMessageContainer" class="mt-4" style="display: none;">
                <div id="updateAlertMessage" class="alert alert-danger" role="alert"></div>
            </div>
        </div>
    </div>
</div>


<script src="update_exercise_modal.js"></script>
<script src="create_exercise_modal.js"></script>

<!-- Custom Scripts -->
<script>

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded');
    fetchExercises();
});

async function fetchExercises() {

     try {
        const exercisesResponse = await fetch('/exercisesByUserIdAndSentenceId?userId=1');
        const exercises = await exercisesResponse.json();
        renderExercises(exercises);
    } catch (error) {
        console.error('Error fetching exercises:', error);
    }
}

function renderExercises(exercises) {
    const exercisesList = document.getElementById('exercisesList');
    exercisesList.innerHTML = ''; // Clear any existing exercises

    if (exercises && exercises.length > 0) {
        exercises.forEach(exercise => {
            const card = createExerciseCard(exercise);
            exercisesList.appendChild(card);
        });
    } else {
        exercisesList.innerHTML = '<p>No exercises found.</p>';
    }
}

let allTopics = [];
let allRules = [];

async function fetchTopicsAndRules() {
     try {
         const topicsResponse = await fetch('/topics?userId=1');
         allTopics = await topicsResponse.json();
         console.log('Fetched topics:', allTopics);

         const rulesResponse = await fetch('/rulesByUserId?userId=1');
         allRules = await rulesResponse.json();
         console.log('Fetched rules:', allRules);

         // Set up dropdowns for initial input fields
         const initialTopicInput = document.querySelector('#topicsContainer .dropdown-input');
         const initialTopicDropdown = document.querySelector('#topicsContainer .dropdown-menu');
         setupDropdown(initialTopicInput, initialTopicDropdown, allTopics);

         const initialRuleInput = document.querySelector('#rulesContainer .dropdown-input');
         const initialRuleDropdown = document.querySelector('#rulesContainer .dropdown-menu');
         setupDropdown(initialRuleInput, initialRuleDropdown, allRules);
     } catch (error) {
         console.error('Error fetching topics or rules:', error);
     }
 }

function createExerciseCard(exercise) {
    const card = document.createElement('div');
    card.className = 'col-md-4';

    const cardTitle = exercise.exerciseName || 'Unnamed Exercise';

    // Extract names of topics
    const topics = exercise.currentTopicsIds.map(topic => topic.name).join(', ') || 'No topics available';

    // Extract names of rules
    const rules = exercise.currentRulesIds.map(rule => rule.name).join(', ') || 'No rules available';

    const exerciseId = exercise.exerciseId;

    const cardContent = `
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title"></h5>
                <p class="card-text"><strong>Topics:</strong> </p>
                <p class="card-text"><strong>Rules:</strong> </p>
                <p class="card-text"><strong>Number of Sentences:</strong> </p>
                <button class="btn btn-primary start-btn">Start</button>
                <button class="btn btn-warning update-btn">Update</button>
                <button class="btn btn-danger delete-btn">Delete</button>
            </div>
        </div>
    `;

    const numberOfSentences = exercise.numberOfSentences || 'No sentences';

    card.innerHTML = cardContent;
    card.querySelector('.card-title').textContent = cardTitle;
    card.querySelector('.card-text:nth-of-type(1)').append(document.createTextNode(topics));
    card.querySelector('.card-text:nth-of-type(2)').append(document.createTextNode(rules));
    card.querySelector('.card-text:nth-of-type(3)').append(document.createTextNode(numberOfSentences));

    const startButton = card.querySelector('.start-btn');
    startButton.addEventListener('click', async function() {
        try {
            // Start lesson and get response
            const response = await activateExercise(exercise.exerciseId);
            window.location.href = '/lesson-form';
        } catch (error) {
            console.error('Error starting or posting lesson:', error);
        }
    });

    const updateButton = card.querySelector('.update-btn');
    updateButton.addEventListener('click', function() {
        console.log('Update button clicked for exercise:', exerciseId);
        openUpdateModal(exerciseId); // Call the function to open the update modal
    });

    const deleteButton = card.querySelector('.delete-btn');
    deleteButton.addEventListener('click', async function() {
        // Implement the delete exercise logic here
        console.log('Delete button clicked for exercise:', exerciseId);
        try {
            await deleteExercise(exerciseId);
            card.remove(); // Remove the card from the DOM after successful deletion
            fetchExercises();
        } catch (error) {
            console.error('Error deleting exercise:', error);
        }
    });

    return card;
}

async function deleteExercise(exerciseId) {
    // Logic to delete the exercise (e.g., API call to delete the exercise)
    console.log('Deleting exercise:', exerciseId);
    // Example API call (replace with actual implementation)
    const response = await fetch('/exercises/'+exerciseId, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete exercise');
    }
}

async function activateExercise(exerciseId) {
    try {
        const url = '/activate/' + exerciseId + '?userId=1';
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Accept': '*/*'
            },
            body: '' // Empty body as per the request
        });

        if (response.ok) {
            console.log('Lesson updated successfully!', data);
        } else {
            console.error('Failed to update lesson:', response.statusText);
        }
    } catch (error) {
        console.error('Error updating lesson:', error);
    }
}

</script>
