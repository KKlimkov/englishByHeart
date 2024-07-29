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


<!-- Custom Scripts -->
<script>

let allTopics = [];
let allRules = [];

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

function createExerciseCard(exercise) {
    const card = document.createElement('div');
    card.className = 'col-md-4';

    const cardTitle = exercise.exerciseName || 'Unnamed Exercise';
    const topics = exercise.currentTopicsIds.length > 0
        ? exercise.currentTopicsIds.join(', ')
        : 'No topics available';

    const rules = exercise.currentRulesIds.length > 0
        ? exercise.currentRulesIds.join(', ')
        : 'No rules available';

    const exerciseId = exercise.exerciseId;

    const cardContent = `
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title"></h5>
                <p class="card-text"><strong>Topics:</strong> </p>
                <p class="card-text"><strong>Rules:</strong> </p>
                <p class="card-text"><strong>Number of Sentences:</strong> </p>
                <button class="btn btn-primary start-btn">Start</button>
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

    return card;
}

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

 function filterItems(inputValue, allItems, dropdownMenu) {
     dropdownMenu.innerHTML = '';
     if (inputValue.length >= 2) {
         const filteredItems = allItems.filter(item => (item.topicName && item.topicName.toLowerCase().includes(inputValue)) || (item.rule && item.rule.toLowerCase().includes(inputValue)));
         filteredItems.forEach(item => {
             const option = document.createElement('a');
             option.classList.add('dropdown-item');
             option.href = '#';
             option.textContent = item.topicName || item.rule;
             option.dataset.id = item.topicId || item.ruleId;
             dropdownMenu.appendChild(option);
         });
     }
 }

 function setupDropdown(input, dropdownMenu, allItems) {
     input.addEventListener('input', function(event) {
         const inputValue = event.target.value.trim().toLowerCase();
         filterItems(inputValue, allItems, dropdownMenu);
         dropdownMenu.classList.add('show');
     });

     input.addEventListener('blur', function() {
         setTimeout(() => {
             dropdownMenu.classList.remove('show');
             const selectedItem = Array.from(dropdownMenu.children).find(child => child.textContent === input.value);
             if (!selectedItem) {
                 input.value = '';
                 input.dataset.id = '';
             } else {
                 input.dataset.id = selectedItem.dataset.id;
             }
         }, 100);
     });

     input.addEventListener('click', function() {
         if (dropdownMenu.innerHTML.trim() !== '') {
             dropdownMenu.classList.add('show');
         }
     });

     dropdownMenu.addEventListener('click', function(event) {
         const target = event.target;
         if (target.classList.contains('dropdown-item')) {
             input.value = target.textContent;
             input.dataset.id = target.dataset.id;
             dropdownMenu.classList.remove('show');
         }
     });

     input.addEventListener('keydown', function(event) {
         if (event.key === 'Enter') {
             event.preventDefault();
         }
     });

     console.log('Dropdown setup for input:', input);
     console.log('Dropdown setup for dropdownMenu:', dropdownMenu);
 }

 function addTopic() {
     const topicsContainer = document.getElementById('topicsContainer');
     const topicInputGroup = document.createElement('div');
     topicInputGroup.classList.add('input-group', 'mb-2');

     const topicInput = document.createElement('input');
     topicInput.type = 'text';
     topicInput.classList.add('form-control', 'dropdown-input');
     topicInput.placeholder = 'Type to search';
     topicInput.dataset.id = '';

     const dropdownMenu = document.createElement('div');
     dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

     setupDropdown(topicInput, dropdownMenu, allTopics);

     const inputGroupAppend = document.createElement('div');
     inputGroupAppend.classList.add('input-group-append');

     const removeButton = document.createElement('button');
     removeButton.classList.add('btn', 'btn-danger');
     removeButton.type = 'button';
     removeButton.textContent = 'Remove Topic';
     removeButton.onclick = function() {
         topicInputGroup.remove();
     };

     inputGroupAppend.appendChild(removeButton);
     topicInputGroup.appendChild(topicInput);
     topicInputGroup.appendChild(inputGroupAppend);
     topicInputGroup.appendChild(dropdownMenu);
     topicsContainer.appendChild(topicInputGroup);
 }

 function addRule() {
     const rulesContainer = document.getElementById('rulesContainer');
     const ruleInputGroup = document.createElement('div');
     ruleInputGroup.classList.add('input-group', 'mb-2');

     const ruleInput = document.createElement('input');
     ruleInput.type = 'text';
     ruleInput.classList.add('form-control', 'dropdown-input');
     ruleInput.placeholder = 'Type to search rule';
     ruleInput.dataset.id = '';

     const dropdownMenu = document.createElement('div');
     dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

     setupDropdown(ruleInput, dropdownMenu, allRules);

     const inputGroupAppend = document.createElement('div');
     inputGroupAppend.classList.add('input-group-append');

     const removeButton = document.createElement('button');
     removeButton.classList.add('btn', 'btn-danger');
     removeButton.type = 'button';
     removeButton.textContent = 'Remove Rule';
     removeButton.onclick = function() {
         ruleInputGroup.remove();
     };

     inputGroupAppend.appendChild(removeButton);
     ruleInputGroup.appendChild(ruleInput);
     ruleInputGroup.appendChild(inputGroupAppend);
     ruleInputGroup.appendChild(dropdownMenu);
     rulesContainer.appendChild(ruleInputGroup);
 }

 function removeTopic(button) {
     button.closest('.input-group').remove();
 }

 function removeRule(button) {
     button.closest('.input-group').remove();
 }

 // Clear the modal fields and reinitialize dropdowns when the modal is shown
 document.getElementById('addExerciseModal').addEventListener('show.bs.modal', async function() {
     await fetchTopicsAndRules();
     document.getElementById('sentenceName').value = '';
     document.getElementById('topicsContainer').innerHTML = `<div class="input-group mb-2">
         <input type="text" class="form-control dropdown-input" placeholder="Type to search" data-topic-id="">
         <div class="input-group-append">
             <button class="btn btn-danger" type="button" onclick="removeTopic(this)">Remove Topic</button>
         </div>
         <div class="dropdown-menu scroll-container"></div>
     </div>`;
     document.getElementById('rulesContainer').innerHTML = `<div class="input-group mb-2">
         <input type="text" class="form-control dropdown-input" placeholder="Type to search rule" data-rule-id="">
         <div class="input-group-append">
             <button class="btn btn-danger" type="button" onclick="removeRule(this)">Remove Rule</button>
         </div>
         <div class="dropdown-menu scroll-container"></div>
     </div>`;

     // Reinitialize dropdowns for the new inputs
     const initialTopicInput = document.querySelector('#topicsContainer .dropdown-input');
     const initialTopicDropdown = document.querySelector('#topicsContainer .dropdown-menu');
     setupDropdown(initialTopicInput, initialTopicDropdown, allTopics);

     const initialRuleInput = document.querySelector('#rulesContainer .dropdown-input');
     const initialRuleDropdown = document.querySelector('#rulesContainer .dropdown-menu');
     setupDropdown(initialRuleInput, initialRuleDropdown, allRules);
 });

 // Add validation for the "Name" field
 // Update the Add Exercise Function
document.getElementById('addExerciseBtn').addEventListener('click', async function() {
    const exerciseName = document.getElementById('exerciseName').value.trim();
    if (!exerciseName) {
        document.getElementById('exerciseName').classList.add('is-invalid');
        showError('Name field is required.');
        return;
    } else {
        document.getElementById('exerciseName').classList.remove('is-invalid');
    }

    const topicInputs = document.querySelectorAll('#topicsContainer .dropdown-input');
    const ruleInputs = document.querySelectorAll('#rulesContainer .dropdown-input');

    const topicIds = [];
    topicInputs.forEach(input => {
        if (input.dataset.id) {
            topicIds.push(input.dataset.id);
        }
    });

    const ruleIds = [];
    ruleInputs.forEach(input => {
        if (input.dataset.id) {
            ruleIds.push(input.dataset.id);
        }
    });

    try {
        const payload = {
            userId: 1,
            topicIds: topicIds,
            ruleIds: ruleIds,
            exerciseName: exerciseName
        };

        const response = await fetch('/createExercise', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            console.log('Exercise created successfully!');
            hideError(); // Hide the error message

            // Close the modal using Bootstrap 5 modal method
            const addExerciseModalElement = document.getElementById('addExerciseModal');
            const addExerciseModal = bootstrap.Modal.getInstance(addExerciseModalElement); // Get the Bootstrap modal instance
            // addExerciseModal.hide();
            $('#addExerciseModal').modal('hide');
            $('.modal-backdrop').remove();

            // Reload the exercises list
            await fetchExercises();
        } else {
            const responseBody = await response.json();
            console.error('Failed to create exercise:', responseBody.message || response.statusText);
            showError(responseBody.message || 'Something went wrong'); // Display server message or fallback
        }
    } catch (error) {
        console.error('Error creating exercise:', error);
        showError('Error creating exercise. Please try again.');
    }
});


 function showError(message) {
     const messageContainer = document.getElementById('messageContainer');
     const alertMessage = document.getElementById('alertMessage');
     alertMessage.textContent = message;
     messageContainer.style.display = 'block';
 }

 function hideError() {
     const messageContainer = document.getElementById('messageContainer');
     messageContainer.style.display = 'none';
 }

</script>
