(function() {
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
        } catch (error) {
            console.error('Error fetching topics or rules:', error);
        }
    }

    async function fetchExerciseDetails(exerciseId) {
        try {
            const response = await fetch(`/exercisesByUserIdAndSentenceId?exerciseId=${exerciseId}`);
            const data = await response.json();
            return data[0]; // Assuming the response is an array with one object
        } catch (error) {
            console.error('Error fetching exercise details:', error);
        }
    }


function filterItems(inputValue, allItems, dropdownMenu, dataAttribute) {
    dropdownMenu.innerHTML = '';
    if (inputValue.length >= 2) {
        const filteredItems = allItems.filter(item =>
            (item.topicName && item.topicName.toLowerCase().includes(inputValue)) ||
            (item.rule && item.rule.toLowerCase().includes(inputValue))
        );
        filteredItems.forEach(item => {
            const option = document.createElement('a');
            option.classList.add('dropdown-item');
            option.href = '#';
            option.textContent = item.topicName || item.rule;
            option.dataset[dataAttribute] = item.topicId || item.ruleId; // Use dataAttribute
            dropdownMenu.appendChild(option);
        });
    }
}

    function addTopic(topicName = '') {
        const container = document.getElementById('topicsContainer');
        const topicInputGroup = createInputGroup(topicName, 'topicId', allTopics);
        container.appendChild(topicInputGroup);
    }

    function addUpdateTopic(topic = {}) {
        const container = document.getElementById('updateTopicsContainer');
        const topicName = topic.name || ''; // Handle case when topic is undefined
        const topicInputGroup = createInputGroup(topicName, 'topicId', allTopics);
        if (topic.id) {
            topicInputGroup.querySelector('.dropdown-input').dataset.topicId = topic.id;
        }
        container.appendChild(topicInputGroup);
    }

    function addRule(ruleName = '') {
        const container = document.getElementById('rulesContainer');
        const ruleInputGroup = createInputGroup(ruleName, 'ruleId', allRules);
        container.appendChild(ruleInputGroup);
    }

    function addUpdateRule(rule = {}) {
        const container = document.getElementById('updateRulesContainer');
        const ruleName = rule.name || ''; // Handle case when rule is undefined
        const ruleInputGroup = createInputGroup(ruleName, 'ruleId', allRules);
        if (rule.id) {
            ruleInputGroup.querySelector('.dropdown-input').dataset.ruleId = rule.id;
        }
        container.appendChild(ruleInputGroup);
    }


    function createInputGroup(name, dataAttribute, items) {
        const inputGroup = document.createElement('div');
        inputGroup.className = 'input-group mb-2';

        const input = document.createElement('input');
        input.type = 'text';
        input.classList.add('form-control', 'dropdown-input');
        input.placeholder = 'Type to search';
        input.value = name;
        input.dataset[dataAttribute] = ''; // Use the passed dataAttribute

        const dropdownMenu = document.createElement('div');
        dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

        setupDropdown(input, dropdownMenu, items, dataAttribute); // Pass dataAttribute

        const inputGroupAppend = document.createElement('div');
        inputGroupAppend.classList.add('input-group-append');

        const removeButton = document.createElement('button');
        removeButton.classList.add('btn', 'btn-danger');
        removeButton.type = 'button';
        removeButton.textContent = 'Remove';
        removeButton.onclick = function() { inputGroup.remove(); };

        inputGroupAppend.appendChild(removeButton);
        inputGroup.appendChild(input);
        inputGroup.appendChild(inputGroupAppend);
        inputGroup.appendChild(dropdownMenu);

        return inputGroup;
    }

    async function updateExercise(exerciseId) {
              const nameInput = document.getElementById('updateExerciseName');
              const name = nameInput.value.trim();
              const topicInputs = Array.from(document.querySelectorAll('#updateTopicsContainer .dropdown-input'));
              const ruleInputs = Array.from(document.querySelectorAll('#updateRulesContainer .dropdown-input'));

              let hasError = false;

              // Clear previous error states
              clearErrorStates();

              // Check if name is empty
              if (!name) {
                  nameInput.classList.add('is-invalid');
                  showError('Exercise name cannot be empty.');
                  hasError = true;
              }

              // Check if all topic inputs have valid values
              const topics = topicInputs.map(input => input.dataset.topicId).filter(id => id);
              if (topics.length !== topicInputs.length) {
                  topicInputs.forEach(input => {
                      if (!input.dataset.topicId) {
                          input.classList.add('is-invalid');
                      }
                  });
                  showError('All selected topics must be valid.');
                  hasError = true;
              }

              // Check if all rule inputs have valid values
              const rules = ruleInputs.map(input => input.dataset.ruleId).filter(id => id);
              if (rules.length !== ruleInputs.length) {
                  ruleInputs.forEach(input => {
                      if (!input.dataset.ruleId) {
                          input.classList.add('is-invalid');
                      }
                  });
                  showError('All selected rules must be valid.');
                  hasError = true;
              }

              if (hasError) return;

              const updatedExercise = {
                  userId: 1,
                  exerciseName: name,
                  topicsIds: topics,
                  rulesIds: rules
              };

              try {
                  const response = await fetch(`/updateExerciseByExerciseId/${exerciseId}`, {
                      method: 'PUT',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(updatedExercise)
                  });

                  if (response.ok) {
                      // Close the modal using Bootstrap's method
                      const updateModal = bootstrap.Modal.getInstance(document.getElementById('updateExerciseModal'));
                      if (updateModal) {
                          updateModal.hide();
                      }
                      await updateExercises();
                      await fetchExercises();
                  } else {
                      showError('Failed to update exercise.');
                  }
              } catch (error) {
                  console.error('Error updating exercise:', error);
                  showError('Error updating exercise.');
              }
          }


    function showError(message) {
        const messageContainer = document.getElementById('updateMessageContainer');
        const alertMessage = document.getElementById('updateAlertMessage');
        alertMessage.textContent = message;
        messageContainer.style.display = 'block';
    }

    function clearErrorStates() {
        // Remove error borders from inputs
        document.querySelectorAll('.invalid-input').forEach(input => {
            input.classList.remove('invalid-input');
        });

        // Clear error message
        const messageContainer = document.getElementById('updateMessageContainer');
        messageContainer.style.display = 'none';
    }

    function openUpdateModal(exerciseId) {
        fetchExerciseDetails(exerciseId).then(exercise => {
            if (exercise) {
                clearErrorStates(); // Clear previous errors when opening the modal
                document.getElementById('updateExerciseName').value = exercise.exerciseName;
                const topicsContainer = document.getElementById('updateTopicsContainer');
                topicsContainer.innerHTML = '';
                exercise.currentTopicsIds.forEach(topic => addUpdateTopic(topic));
                const rulesContainer = document.getElementById('updateRulesContainer');
                rulesContainer.innerHTML = '';
                exercise.currentRulesIds.forEach(rule => addUpdateRule(rule));
                const updateBtn = document.getElementById('updateExerciseBtn');
                updateBtn.onclick = function() { updateExercise(exerciseId); };
                const updateModal = new bootstrap.Modal(document.getElementById('updateExerciseModal'));
                updateModal.show();
            }
        });
    }


    function setupDropdown(input, dropdownMenu, allItems, dataAttribute) {
        input.addEventListener('input', function(event) {
            const inputValue = event.target.value.trim().toLowerCase();
            filterItems(inputValue, allItems, dropdownMenu, dataAttribute); // Pass dataAttribute
            dropdownMenu.classList.add('show');
        });

        input.addEventListener('blur', function() {
            setTimeout(() => {
                dropdownMenu.classList.remove('show');
                const selectedItem = Array.from(dropdownMenu.children).find(child => child.textContent === input.value);
                if (!selectedItem) {
                    input.value = '';
                    input.dataset[dataAttribute] = ''; // Use dataAttribute
                } else {
                    input.dataset[dataAttribute] = selectedItem.dataset[dataAttribute]; // Use dataAttribute
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
                input.dataset[dataAttribute] = target.dataset[dataAttribute]; // Use dataAttribute
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

    window.openUpdateModal = openUpdateModal;
    window.addTopic = addTopic;
    window.addUpdateTopic = addUpdateTopic;
    window.addRule = addRule;
    window.addUpdateRule = addUpdateRule;

    // Initialize
    fetchTopicsAndRules();
})();
