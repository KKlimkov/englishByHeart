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
            <div class="custom-dropdown topic-container">
                <input type="text" class="form-control dropdown-input" id="topicInput" name="topicInput" placeholder="Type to search">
                <div id="dropdownMenu" class="dropdown-menu scroll-container" role="menu"></div>
            </div>
            <input type="hidden" id="topicIds" name="topicIds">
        </div>

        <div id="additionalTopics"></div>

        <button type="button" class="btn btn-secondary mb-3" onclick="addTopicContainer()">Add Another Topic</button>

        <div class="mb-3">
            <h3>Translations</h3>
            <div id="translations"></div>
            <button type="button" class="btn btn-secondary mb-3" onclick="addTranslation()">Add Translation</button>
        </div>

        <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>

        <div id="messageContainer" class="container mt-4"></div>

    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
    let allTopics = [];

    const topicInput = document.getElementById('topicInput');
    const dropdownMenu = document.getElementById('dropdownMenu');

    fetch('/topics?userId=1')
        .then(response => response.json())
        .then(topics => {
            allTopics = topics;
            filterTopics('');
        })
        .catch(error => console.error('Error fetching topics:', error));

    topicInput.addEventListener('input', function(event) {
        const inputValue = event.target.value.trim().toLowerCase();
        filterTopics(inputValue);
    });

    topicInput.addEventListener('click', function() {
        dropdownMenu.style.display = 'block';
    });

    topicInput.addEventListener('blur', function() {
        setTimeout(() => {
            const inputValue = topicInput.value.trim().toLowerCase();
            const matchingTopic = allTopics.find(topic => topic.topicName.toLowerCase() === inputValue);
            if (!matchingTopic) {
                topicInput.value = '';
            }
            dropdownMenu.style.display = 'none';
        }, 100);
    });

    dropdownMenu.addEventListener('click', function(event) {
        const target = event.target;
        if (target.classList.contains('dropdown-item')) {
            topicInput.value = target.textContent;
            dropdownMenu.style.display = 'none';
        }
    });

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
        topicContainer.appendChild(dropdownMenu);

        topicInput.addEventListener('input', function(event) {
            const inputValue = event.target.value.trim().toLowerCase();
            filterTopicsForContainer(inputValue, dropdownMenu, topics);
        });

        topicInput.addEventListener('click', function() {
            dropdownMenu.style.display = 'block';
        });

        topicInput.addEventListener('blur', function() {
            setTimeout(() => {
                const inputValue = topicInput.value.trim().toLowerCase();
                const matchingTopic = topics.find(topic => topic.topicName.toLowerCase() === inputValue);
                if (!matchingTopic) {
                    topicInput.value = '';
                }
                dropdownMenu.style.display = 'none';
            }, 100);
        });

        dropdownMenu.addEventListener('click', function(event) {
            const target = event.target;
            if (target.classList.contains('dropdown-item')) {
                topicInput.value = target.textContent;
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


</script>
