<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Sentence</h1>
    <form id="sentenceForm">
        <div class="mb-3">
            <label for="learningSentence" class="form-label">Learning Sentence:</label>
            <input type="text" class="form-control" id="learningSentence" name="learningSentence" required>
        </div>
        <div class="mb-3">
            <label for="comment" class="form-label">Comment:</label>
            <input type="text" class="form-control" id="comment" name="comment">
        </div>
        <div class="mb-3">
            <label for="userLink" class="form-label">User Link:</label>
            <input type="text" class="form-control" id="userLink" name="userLink">
        </div>
        <!-- Add any other input fields as needed -->

        <div id="topicContainer" class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <div class="custom-dropdown">
                <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search">
                <div id="dropdownMenu" class="dropdown-menu scroll-container" role="menu">
                    <!-- Dropdown menu items will be populated dynamically -->
                </div>
            </div>
            <input type="hidden" id="topicIds" name="topicIds">
        </div>

        <div class="mb-3">
            <label for="topicInput" class="form-label">Topic:</label>
            <div id="additionalTopics" class="dropdown">
                <!-- New topic containers will be appended here -->
            </div>
        </div>

        <button type="button" class="btn btn-secondary mb-3" onclick="addTopicContainer()">Add Another Topic</button>

        <div class="mb-3">
            <h3>Translations</h3>
            <div id="translations"></div>
            <button type="button" class="btn btn-secondary mb-3" onclick="addTranslation()">Add Translation</button>
        </div>

        <div>
            <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>
        </div>

        <div id="messageContainer" class="container mt-4"></div>

    </form>
</div>

<script>
    const topicIdsInput = document.getElementById('topicIds');

    document.addEventListener('click', function(event) {


        async function addTopicContainer() {
        try {
            // Fetch topics data from the server
            const response = await fetch('/topics?userId=1');
            if (!response.ok) {
                throw new Error('Failed to fetch topics');
            }
            const topics = await response.json();

            const additionalTopicsDiv = document.getElementById('additionalTopics');

            // Create a new topic container
            const topicContainer = await createTopicContainer(topics);

            // Append the new topic container to the additionalTopicsDiv
            additionalTopicsDiv.appendChild(topicContainer);

            // Check if the topicContainer exists and has the topicInput element
            const topicInput = topicContainer.querySelector('.dropdown-input');
            if (topicInput) {
                const dropdownMenu = topicContainer.querySelector('.dropdown-menu');
                topicInput.addEventListener('click', function() {
                    dropdownMenu.style.display = 'block';
                });

                document.addEventListener('click', function(event) {
                    // Check if the click occurred outside of the dropdown menu and the input field
                    if (!dropdownMenu.contains(event.target) && event.target !== topicInput) {
                        dropdownMenu.style.display = 'none';
                    }
                });
            } else {
                console.error('Topic input element not found in the topic container.');
            }
        } catch (error) {
            console.error('Error fetching topics:', error);
        }
    }
        const addTopicButton = document.getElementById('addTopicButton');
        addTopicButton.addEventListener('click', addTopicContainer);

    });

    document.addEventListener('DOMContentLoaded', function() {
        const topicInput = document.getElementById('topicInput');
        const dropdownMenu = document.getElementById('dropdownMenu');

       document.addEventListener('click', function(event) {
        // Check if the click occurred outside of the dropdown menu and input field
        if (!dropdownMenu.contains(event.target) && event.target !== topicInput) {
            dropdownMenu.style.display = 'none';
        }
    });

        let allTopics = [];

        topicInput.addEventListener('input', function(event) {
            const inputValue = event.target.value.trim().toLowerCase();
            filterTopics(inputValue);
        });

        topicInput.addEventListener('click', function() {
            dropdownMenu.style.display = 'block';
        });

        dropdownMenu.addEventListener('click', function(event) {
            const target = event.target;
            if (target.classList.contains('dropdown-item')) {
                topicInput.value = target.textContent;
                dropdownMenu.style.display = 'none';
            }
        });

        document.addEventListener('click', function(event) {
            if (!dropdownMenu.contains(event.target) && event.target !== topicInput) {
                dropdownMenu.style.display = 'none';
            }
        });

        fetch('/topics?userId=1')
            .then(response => response.json())
            .then(topics => {
                allTopics = topics;
                filterTopics('');
            })
            .catch(error => console.error('Error fetching topics:', error));

        function filterTopics(inputValue) {
            dropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredTopics = allTopics.filter(topic => topic.topicName.toLowerCase().includes(inputValue));
                filteredTopics.forEach(topic => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = topic.topicName;
                    dropdownMenu.appendChild(option);
                });
            }
        }

        window.addTopicContainer = async function() {
            try {
                const response = await fetch('/topics?userId=1');
                if (!response.ok) throw new Error('Failed to fetch topics');
                const topics = await response.json();
                const additionalTopicsDiv = document.getElementById('additionalTopics');
                const topicContainer = await createTopicContainer(topics);
                additionalTopicsDiv.appendChild(topicContainer);
            } catch (error) {
                console.error('Error fetching topics:', error);
            }
        }

        async function createTopicContainer(topics) {
            const topicContainer = document.createElement('div');
            topicContainer.classList.add('topic-container', 'mb-3');
            topicContainer.style.border = '1px solid #ccc';
            topicContainer.style.padding = '10px';
            topicContainer.style.borderRadius = '5px';

            const topicInput = document.createElement('input');
            topicInput.type = 'text';
            topicInput.classList.add('form-control', 'dropdown-input');
            topicInput.placeholder = 'Type to search';
            topicContainer.appendChild(topicInput);

            const removeButton = document.createElement('button');
            removeButton.textContent = 'Remove Topic';
            removeButton.classList.add('btn', 'btn-danger');
            removeButton.style.marginLeft = '10px';
            removeButton.onclick = function() {
                topicContainer.remove();
            };
            topicContainer.appendChild(removeButton);

            const dropdownMenu = document.createElement('div');
            dropdownMenu.classList.add('dropdown-menu', 'scroll-container');
            dropdownMenu.style.display = 'none';
            topicContainer.appendChild(dropdownMenu);

            topicInput.addEventListener('input', function(event) {
                const inputValue = event.target.value.trim().toLowerCase();
                filterTopicsForContainer(inputValue, dropdownMenu, topics);
            });

            topicInput.addEventListener('click', function() {
                dropdownMenu.style.display = 'block';
            });

            dropdownMenu.addEventListener('click', function(event) {
                const target = event.target;
                if (target.classList.contains('dropdown-item')) {
                    topicInput.value = target.textContent;
                    dropdownMenu.style.display = 'none';
                }
            });

            document.addEventListener('click', function(event) {
                if (!dropdownMenu.contains(event.target) && event.target !== topicInput) {
                    dropdownMenu.style.display = 'none';
                }
            });

            return topicContainer;
        }

        function filterTopicsForContainer(inputValue, dropdownMenu, topics) {
            dropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredTopics = topics.filter(topic => topic.topicName.toLowerCase().includes(inputValue));
                filteredTopics.forEach(topic => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = topic.topicName;
                    dropdownMenu.appendChild(option);
                });
            }
        }
    });

    function addTranslation() {
        const translationsDiv = document.getElementById('translations');
        const translationInput = document.createElement('input');
        translationInput.type = 'text';
        translationInput.classList.add('form-control', 'mb-2');
        translationsDiv.appendChild(translationInput);
    }

</script>