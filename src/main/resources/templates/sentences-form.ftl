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
            <button type="button" class="btn btn-secondary mb-3" onclick="addTranslationContainer()">Add Translation</button>
        </div>

        <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>

        <div id="messageContainer" class="container mt-4"></div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        let allTopics = [];
        let allRules = [];

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

        topicInput.addEventListener('focus', function() {
            if (dropdownMenu.innerHTML.trim() !== '') {
                dropdownMenu.style.display = 'block';
            }
        });

        topicInput.addEventListener('blur', function() {
            setTimeout(() => {
                dropdownMenu.style.display = 'none';
            }, 100);
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

        function filterTopicsForContainer(inputValue, containerDropdownMenu, topics) {
            containerDropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredTopics = topics.filter(topic => topic.topicName.toLowerCase().includes(inputValue));
                filteredTopics.forEach(topic => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = topic.topicName;
                    containerDropdownMenu.appendChild(option);
                });
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

        fetch('/rulesByUserId?userId=1')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch rules');
            }
            return response.json();
        })
        .then(rules => {
            allRules = rules;
        })
        .catch(error => console.error('Error fetching rules:', error));

        function filterRules(inputValue, dropdownMenu) {
            dropdownMenu.innerHTML = '';
            if (inputValue.length >= 2) {
                const filteredRules = allRules.filter(rule => rule.rule.toLowerCase().includes(inputValue));
                filteredRules.forEach(rule => {
                    const option = document.createElement('a');
                    option.classList.add('dropdown-item');
                    option.href = '#';
                    option.textContent = rule.rule;
                    dropdownMenu.appendChild(option);
                });
            }
        }

        async function createTranslationContainer() {
            const translationContainer = document.createElement('div');
            translationContainer.classList.add('translation-container', 'mb-3');
            translationContainer.style.border = '1px solid #ccc';
            translationContainer.style.padding = '10px';
            translationContainer.style.borderRadius = '5px';

            const translationInput = document.createElement('input');
            translationInput.type = 'text';
            translationInput.classList.add('form-control', 'mb-3');
            translationInput.placeholder = 'Translation';
            translationContainer.appendChild(translationInput);

            const ruleInput = document.createElement('input');
            ruleInput.type = 'text';
            ruleInput.classList.add('form-control', 'dropdown-input', 'mb-3');
            ruleInput.placeholder = 'Type to search rule';
            translationContainer.appendChild(ruleInput);

            const ruleDropdownMenu = document.createElement('div');
            ruleDropdownMenu.classList.add('dropdown-menu', 'scroll-container');
            translationContainer.appendChild(ruleDropdownMenu);

            ruleInput.addEventListener('input', function(event) {
                const inputValue = event.target.value.trim().toLowerCase();
                filterRules(inputValue, ruleDropdownMenu);
                ruleDropdownMenu.style.display = 'block';
            });

            ruleInput.addEventListener('focus', function() {
                ruleDropdownMenu.style.display = 'block';
            });

            ruleInput.addEventListener('blur', function() {
                setTimeout(() => {
                    ruleDropdownMenu.style.display = 'none';
                }, 100);
            });

            ruleDropdownMenu.addEventListener('click', function(event) {
                const target = event.target;
                if (target.classList.contains('dropdown-item')) {
                    ruleInput.value = target.textContent;
                    ruleDropdownMenu.style.display = 'none';
                }
            });

            const addRuleButton = document.createElement('button');
            addRuleButton.textContent = 'Add Rule';
            addRuleButton.classList.add('btn', 'btn-primary', 'mb-3');
            addRuleButton.onclick = function() {
                const ruleContainer = document.createElement('div');
                ruleContainer.classList.add('rule-container', 'mb-3');
                const ruleInput = document.createElement('input');
                ruleInput.type = 'text';
                ruleInput.classList.add('form-control', 'dropdown-input', 'mb-3');
                ruleInput.placeholder = 'Type to search rule';

                const ruleDropdownMenu = document.createElement('div');
                ruleDropdownMenu.classList.add('dropdown-menu', 'scroll-container');
                ruleContainer.appendChild(ruleInput);
                ruleContainer.appendChild(ruleDropdownMenu);

                ruleInput.addEventListener('input', function(event) {
                    const inputValue = event.target.value.trim().toLowerCase();
                    filterRules(inputValue, ruleDropdownMenu);
                    ruleDropdownMenu.style.display = 'block';
                });

                ruleInput.addEventListener('focus', function() {
                    ruleDropdownMenu.style.display = 'block';
                });

                ruleInput.addEventListener('blur', function() {
                    setTimeout(() => {
                        ruleDropdownMenu.style.display = 'none';
                    }, 100);
                });

                ruleDropdownMenu.addEventListener('click', function(event) {
                    const target = event.target;
                    if (target.classList.contains('dropdown-item')) {
                        ruleInput.value = target.textContent;
                        ruleDropdownMenu.style.display = 'none';
                    }
                });

                const removeRuleButton = document.createElement('button');
                removeRuleButton.textContent = 'Remove Rule';
                removeRuleButton.classList.add('btn', 'btn-danger', 'mb-3');
                removeRuleButton.onclick = function() {
                    ruleContainer.remove();
                };

                ruleContainer.appendChild(removeRuleButton);
                translationContainer.appendChild(ruleContainer);
            };

            const removeTranslationButton = document.createElement('button');
            removeTranslationButton.textContent = 'Remove Translation';
            removeTranslationButton.classList.add('btn', 'btn-danger');
            removeTranslationButton.onclick = function() {
                translationContainer.remove();
            };

            translationContainer.appendChild(addRuleButton);
            translationContainer.appendChild(removeTranslationButton);

            return translationContainer;
        }

        window.addTranslationContainer = async function() {
            const translationsDiv = document.getElementById('translations');
            const translationContainer = await createTranslationContainer();
            translationsDiv.appendChild(translationContainer);
        }

        window.submitForm = function() {
            const form = document.getElementById('sentenceForm');
            const formData = new FormData(form);

            fetch('/submitSentence', {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to submit sentence');
                    }
                    return response.json();
                })
                .then(data => {
                    const messageContainer = document.getElementById('messageContainer');
                    messageContainer.innerHTML = '<div class="alert alert-success">Sentence submitted successfully</div>';
                    form.reset();
                })
                .catch(error => {
                    const messageContainer = document.getElementById('messageContainer');
                    messageContainer.innerHTML = '<div class="alert alert-danger">Error submitting sentence: ' + error.message + '</div>';
                });
        }
    });
</script>

