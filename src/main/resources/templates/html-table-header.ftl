<html>
<head>
    <title>${pageTitle}</title>
    <style>
        .Report {
            margin: 0;
            padding: 0;
            width: 100%;
            box-shadow: 10px 10px 5px #888888;
            border: 1px solid #000000;

            -moz-border-radius-bottomleft: 0;
            -webkit-border-bottom-left-radius: 0;
            border-bottom-left-radius: 0;

            -moz-border-radius-bottomright: 0;
            -webkit-border-bottom-right-radius: 0;
            border-bottom-right-radius: 0;

            -moz-border-radius-topright: 0;
            -webkit-border-top-right-radius: 0;
            border-top-right-radius: 0;

            -moz-border-radius-topleft: 0;
            -webkit-border-top-left-radius: 0;
            border-top-left-radius: 0;
        }

        .Report table {
            border-collapse: collapse;
            border-spacing: 0;
            width: 100%;
            margin: 0;
            padding: 0;
        }

        .Report tr:last-child td:last-child {
            -moz-border-radius-bottomright: 0;
            -webkit-border-bottom-right-radius: 0;
            border-bottom-right-radius: 0;
        }

        .Report table tr:first-child td:first-child {
            -moz-border-radius-topleft: 0;
            -webkit-border-top-left-radius: 0;
            border-top-left-radius: 0;
        }

        .Report table tr:first-child td:last-child {
            -moz-border-radius-topright: 0;
            -webkit-border-top-right-radius: 0;
            border-top-right-radius: 0;
        }

        .Report tr:last-child td:first-child {
            -moz-border-radius-bottomleft: 0;
            -webkit-border-bottom-left-radius: 0;
            border-bottom-left-radius: 0;
        }

        .Report tr:hover td {
        }

        .Report tr:nth-child(odd) {
            background-color: #e5e5e5;
        }

        .Report tr:nth-child(even) {
            background-color: #ffffff;
        }

        .Report td {
            vertical-align: middle;

            border: 1px solid #000000;
            border-width: 0 1px 1px 0;
            text-align: left;
            padding: 7px;
            font-size: 10px;
            font-family: Arial,serif;
            font-weight: normal;
            color: #000000;
        }

        .Report tr:last-child td {
            border-width: 0 1px 0 0;
        }

        .Report tr td:last-child {
            border-width: 0 0 1px 0;
        }

        .Report tr:last-child td:last-child {
            border-width: 0 0 0 0;
        }

        .Report tr:first-child td {
            background: -o-linear-gradient(bottom, #aad4ff 5%, #56aaff 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #aad4ff), color-stop(1, #56aaff));
            background: -moz-linear-gradient(center top, #aad4ff 5%, #56aaff 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#aad4ff", endColorstr="#56aaff");
            background: -o-linear-gradient(top, #aad4ff, 56 aaff);

            background-color: #aad4ff;
            border: 0px solid #000000;
            text-align: center;
            border-width: 0 0 1px 1px;
            font-size: 14px;
            font-family: Arial,serif;
            font-weight: bold;
            color: #ffffff;
        }

        .Report tr:first-child:hover td {
            background: -o-linear-gradient(bottom, #aad4ff 5%, #56aaff 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #aad4ff), color-stop(1, #56aaff));
            background: -moz-linear-gradient(center top, #aad4ff 5%, #56aaff 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#aad4ff", endColorstr="#56aaff");
            background: -o-linear-gradient(top, #aad4ff, 56 aaff);

            background-color: #aad4ff;
        }

        .Report tr:first-child td:first-child {
            border-width: 0 0 1px 0;
        }

        .Report tr:first-child td:last-child {
            border-width: 0 0 1px 1px;
        }
    </style>
</head>
<body>
<div class="Report" >
<table>
