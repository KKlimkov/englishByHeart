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
            position: relative;
        }

        .topic-container .form-control {
            flex-grow: 1;
            color: #000;
            position: relative;
            z-index: 1;
        }

        .topic-container .btn-danger {
            margin-left: 10px;
            flex-shrink: 0;
        }

        .dropdown-menu {
            display: none;
            position: absolute;
            top: calc(100% + 5px);
            left: 0;
            z-index: 1000;
            width: calc(100% - 20px);
            max-height: 200px;
            overflow-y: auto;
            background-color: #fff;
            border: 1px solid #ccc;
            border-top: none;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .scroll-container {
            max-height: 200px;
            overflow-y: auto;
            z-index: 10;
        }

        .container-below-dropdown {
            padding-top: 220px;
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

        .translation-container {
            position: relative;
        }

        .rule-wrapper {
            position: relative;
        }

        .dropdown-menu.scroll-container {
            position: absolute;
            top: calc(100% + 5px);
            left: 0;
            z-index: 1000;
            display: none;
            float: left;
            min-width: 160px;
            padding: 5px 0;
            margin: 2px 0 0;
            font-size: 14px;
            text-align: left;
            list-style: none;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, .175);
        }

        .dropdown-menu.scroll-container.show {
            display: block;
        }
        .error {
        border-color: red !important;
    }
     .is-invalid {
    border-color: red;

     .modal-dialog-centered {
            display: flex;
            align-items: center;
            min-height: calc(100% - 1rem);
        }

        .modal-content {
            width: 100%;
            max-width: 600px; /* Adjust the max-width to make the modal larger */
            margin: auto;
        }

        .suggestions-box {
        border: 1px solid #ccc;
        max-height: 100px;
        overflow-y: auto;
        position: absolute;
        z-index: 1000;
        background-color: white;
        width: 100%;
    }
    .suggestions-box div {
        padding: 8px;
        cursor: pointer;
    }
    .suggestions-box div:hover {
        background-color: #f0f0f0;
    }
}
    </style>
</head>


<body>
<!-- Content Area -->

<div id="main-content">
    <#include "navbar.ftl">
    <!-- Initial content here -->
</div>
<!-- Bootstrap JS (Optional) -->
<script src="/bootstrap.bundle.min.js"></script>
<!-- Custom Scripts -->
<script>
    // Add your custom JavaScript here
</script>
</body>
</html>