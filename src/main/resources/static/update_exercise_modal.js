(function() {
    let allTopics = [];
    let allRules = [];

    async function fetchTopicsAndRules() {
        try {
            const topicsResponse = await fetch('/topics?userId=1');
            allTopics = await topicsResponse.json();
            const rulesResponse = await fetch('/rulesByUserId?userId=1');
            allRules = await rulesResponse.json();
        } catch (error) {
            console.error('Error fetching topics or rules:', error);
        }
    }

    function openUpdateModal(exerciseId) {
        fetchExerciseDetails(exerciseId).then(exercise => {
            if (exercise) {
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

    function addTopic(topicName = '') {
        const container = document.getElementById('topicsContainer');
        const topicInputGroup = createInputGroup(topicName, 'topicId', allTopics);
        container.appendChild(topicInputGroup);
    }

    function addUpdateTopic(topicName = '') {
        const container = document.getElementById('updateTopicsContainer');
        const topicInputGroup = createInputGroup(topicName, 'topicId', allTopics);
        container.appendChild(topicInputGroup);
    }

    function addRule(ruleName = '') {
        const container = document.getElementById('rulesContainer');
        const ruleInputGroup = createInputGroup(ruleName, 'ruleId', allRules);
        container.appendChild(ruleInputGroup);
    }

    function addUpdateRule(ruleName = '') {
        const container = document.getElementById('updateRulesContainer');
        const ruleInputGroup = createInputGroup(ruleName, 'ruleId', allRules);
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
        input.dataset[dataAttribute] = '';

        const dropdownMenu = document.createElement('div');
        dropdownMenu.classList.add('dropdown-menu', 'scroll-container');

        setupDropdown(input, dropdownMenu, items);

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
        const name = document.getElementById('updateExerciseName').value;
        const topics = Array.from(document.querySelectorAll('#updateTopicsContainer .dropdown-input')).map(input => input.dataset.topicId);
        const rules = Array.from(document.querySelectorAll('#updateRulesContainer .dropdown-input')).map(input => input.dataset.ruleId);
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
                const updateModal = new bootstrap.Modal(document.getElementById('updateExerciseModal'));
                updateModal.hide();
                fetchExercises(); // Refresh the exercises list
            } else {
                const messageContainer = document.getElementById('updateMessageContainer');
                const alertMessage = document.getElementById('updateAlertMessage');
                alertMessage.textContent = 'Failed to update exercise.';
                messageContainer.style.display = 'block';
            }
        } catch (error) {
            console.error('Error updating exercise:', error);
        }
    }

    function setupDropdown(input, dropdown, items) {
        input.addEventListener('input', function() {
            const filter = input.value.toLowerCase();
            dropdown.innerHTML = '';
            items.filter(item => item.name.toLowerCase().includes(filter)).forEach(item => {
                const dropdownItem = document.createElement('a');
                dropdownItem.classList.add('dropdown-item');
                dropdownItem.textContent = item.name;
                dropdownItem.onclick = function() {
                    input.value = item.name;
                    input.dataset.topicId = item.id;
                    input.dataset.ruleId = item.id;
                };
                dropdown.appendChild(dropdownItem);
            });
            dropdown.style.display = 'block';
        });

        document.addEventListener('click', function(event) {
            if (!input.contains(event.target)) {
                dropdown.style.display = 'none';
            }
        });
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

    window.openUpdateModal = openUpdateModal;
    window.addTopic = addTopic;
    window.addUpdateTopic = addUpdateTopic;
    window.addRule = addRule;
    window.addUpdateRule = addUpdateRule;

    // Initialize
    fetchTopicsAndRules();
})();
