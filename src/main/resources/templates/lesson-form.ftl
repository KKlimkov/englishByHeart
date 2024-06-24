<#include "common.ftl">

<div class="container mt-4">
    <h1>Lesson</h1>
    <form method="post" id="lessonForm">
        <div class="mb-3">
            <label class="form-label">Learning Sentence:</label>
            <span>${learningSentence?ifExists}</span>
        </div>
        <div class="mb-3">
            <label for="translation" class="form-label">Type a Translations:</label>
            <input type="text" class="form-control" id="translation" name="translation">
        </div>
    </form>
</div>