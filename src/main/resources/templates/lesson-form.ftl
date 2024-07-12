<#include "common.ftl">

<style>
    .hidden {
        display: none;
    }
</style>

<div class="container mt-4">
    <h1>Lesson</h1>
    <form id="lessonForm">
        <div class="mb-3">
            <label class="form-label">Learning Sentence:</label>
            <span id="learningSentence"></span>
        </div>
        <div class="mb-3">
            <label for="translation" class="form-label">Type a Translation:</label>
            <input type="text" class="form-control" id="translation" name="translation">
        </div>
        <button type="button" class="btn btn-primary" id="checkButton">Check</button>
        <button type="button" class="btn btn-success" id="continueButton" disabled>Continue</button>
        <button type="button" class="btn btn-warning" id="restartButton" disabled>Restart lesson</button>
    </form>
    <div id="resultMessage" class="mt-3"></div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        fetch('http://localhost:8080/api/lesson/startLesson?userId=1', {
            method: 'POST',
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json'
            },
            body: ''
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'Unknown error');
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === 'error') {
                throw new Error(data.message);
            }

            // Store the response data for later use
            window.lessonData = data;

            // Update the learningSentence span with the learning sentence from the response
            document.getElementById('learningSentence').innerText = data.learningSentence;
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('resultMessage').textContent = error.message;
            document.getElementById('resultMessage').className = 'text-danger';
            document.getElementById('lessonForm').classList.add('hidden');
        });
    });

    document.getElementById('checkButton').addEventListener('click', function() {
        const userTranslation = document.getElementById('translation').value.trim().toLowerCase();
        const translations = window.lessonData.translations;

        let isCorrect = false;
        for (const translation of translations) {
            if (translation.translation.trim().toLowerCase() === userTranslation) {
                isCorrect = true;
                break;
            }
        }

        const resultMessage = document.getElementById('resultMessage');
        const continueButton = document.getElementById('continueButton');
        if (isCorrect) {
            resultMessage.textContent = 'Correct';
            resultMessage.className = 'text-success';
            continueButton.disabled = false; // Enable the Continue button
        } else {
            resultMessage.textContent = 'Incorrect';
            resultMessage.className = 'text-danger';
            continueButton.disabled = true; // Disable the Continue button
        }
    });

    document.getElementById('continueButton').addEventListener('click', function() {
        fetch('http://localhost:8080/api/lesson/updateLesson?userId=1', {
            method: 'PUT',
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json'
            },
            body: ''
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to update lesson');
            }
        })
        .then(message => {
            console.log(message);
            const resultMessage = document.getElementById('resultMessage');
            const restartButton = document.getElementById('restartButton');
            if (message === "Lesson updated successfully.") {
                window.location.href = '/lesson-form';
            } else if (message === "All lessons have been done!") {
                resultMessage.textContent = "All lessons have been done!";
                resultMessage.className = 'text-info';
                restartButton.disabled = false; // Enable the Restart lesson button
            }
        })
        .catch(error => console.error('Error:', error));
    });

    document.getElementById('restartButton').addEventListener('click', function() {
        fetch('http://localhost:8080/restart?userId=1', {
            method: 'PUT',
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json'
            },
            body: ''
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to restart lesson');
            }
        })
        .then(message => {
            if (message === "Ok") {
                window.location.href = '/lesson-form';
            } else {
                console.error('Unexpected response:', message);
            }
        })
        .catch(error => console.error('Error:', error));
    });
</script>
