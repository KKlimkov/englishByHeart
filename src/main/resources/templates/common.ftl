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
        .topic-container {
       display: flex;
       align-items: center;
       margin-bottom: 1rem;
       border: 1px solid #ccc;
       padding: 10px;
       border-radius: 5px;
       position: relative; /* Ensure dropdown is positioned relative to the container */
   }

   .topic-container .form-control {
       flex-grow: 1;
       color: #000; /* Set the text color to black */
       position: relative; /* Ensure proper positioning */
       z-index: 1; /* Ensure input field stays above dropdown */
   }

   .topic-container .btn-danger {
       margin-left: 10px;
       flex-shrink: 0;
   }

   .dropdown-menu {
       display: none;
       position: absolute;
       top: 100%; /* Position the dropdown below the input field */
       left: 0;
       z-index: 1000; /* Ensure the dropdown is above other elements */
       width: 100%; /* Adjust width to match input field */
       max-height: 200px; /* Adjust as needed */
       overflow-y: auto;
       background-color: #fff; /* Ensure the dropdown has a white background */
       border: 1px solid #ccc; /* Add border to match the input */
       border-top: none; /* Remove top border to blend with the input */
       box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Add shadow for better visibility */
   }

   .scroll-container {
       max-height: 200px;
       overflow-y: auto;
   }

   .dropdown-item {
       display: block;
       width: 100%;
       padding: 0.25rem 1rem;
       clear: both;
       font-weight: 400;
       color: #212529;
       text-align: inherit;
       white-space: nowrap;
       background-color: transparent;
       border: 0;
       text-decoration: none;
       cursor: pointer;
   }

   .dropdown-item:hover,
   .dropdown-item:focus {
       color: #16181b;
       text-decoration: none;
       background-color: #f8f9fa;
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