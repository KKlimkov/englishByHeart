<#include "common.ftl">

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
        <div id="resultMessage" class="mt-3"></div>
    </form>
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
    .then(response => response.json())
    .then(data => {
        // Store the response data for later use
        window.lessonData = data;

        // Update the learningSentence span with the learning sentence from the response
        document.getElementById('learningSentence').innerText = data.learningSentence;
    })
    .catch(error => console.error('Error:', error));
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
    if (isCorrect) {
        resultMessage.textContent = 'Correct';
        resultMessage.className = 'text-success';
    } else {
        resultMessage.textContent = 'Incorrect';
        resultMessage.className = 'text-danger';
    }
});
</script>
