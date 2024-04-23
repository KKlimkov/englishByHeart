<#include "common.ftl">

<div class="container mt-4">
    <h1>Create Rule</h1>
    <form action="/rules" method="post">
        <div class="mb-3">
            <label for="ruleInput" class="form-label">Rule:</label>
            <input type="text" class="form-control" id="ruleInput" name="rule" required>
        </div>
        <div class="mb-3">
            <label for="linkInput" class="form-label">Link:</label>
            <input type="text" class="form-control" id="linkInput" name="link" required>
        </div>
        <button type="submit" class="btn btn-primary">Create Rule</button>
    </form>
</div>