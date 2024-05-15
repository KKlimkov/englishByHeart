<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Title Here</title>
    <!-- Bootstrap CSS -->
    <link href="/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        .scroll-container {
      width: 400px;
      max-height: 200px;
      position: relative;
      overflow: auto;
    }

    .btn-input {
       display: block;
    }

    .btn-input .btn.form-control {
        text-align: left;
    }

    .btn-input .btn.form-control span:first-child {
       left: 10px;
       overflow: hidden;
       position: absolute;
       right: 25px;
    }

    .btn-input .btn.form-control .caret {
       margin-top: -1px;
       position: absolute;
       right: 10px;
       top: 50%;
    }
    </style>
</head>


<body>
<!-- Content Area -->
<#include "navbar.ftl">

<!-- Bootstrap JS (Optional) -->
<script src="/bootstrap.bundle.min.js"></script>
<!-- Custom Scripts -->
<script>
    // Add your custom JavaScript here
</script>
</body>
</html>