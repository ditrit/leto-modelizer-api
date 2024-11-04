<?php

$METHOD = $_SERVER['REQUEST_METHOD'];
$URI = $_SERVER['REQUEST_URI'];
$uriSegments = explode('/', trim($_SERVER['REQUEST_URI'], '/'));
$type = isset($uriSegments[1]) ? $uriSegments[1] : null;
$requestBody = json_decode(file_get_contents('php://input'), true);

error_log($type);
error_log(file_get_contents('php://input'));

if ($_SERVER['REQUEST_METHOD'] == 'POST' && $type == "message") {
    $contextValue = isset($requestBody["context"]) ? (int) $requestBody["context"] : 0;
    $data = [
        "context" =>1 + $contextValue,
        "message" => "OK"
    ];
    header('Content-Type: application/json');
    $json = json_encode($data);
    echo $json;
    exit;
} else if ($_SERVER['REQUEST_METHOD'] == 'POST' && $type && isset($requestBody['pluginName'])) {
    $fileName = $type . "_" . str_replace("@ditrit/", "", $requestBody['pluginName']) . ".json";
    error_log($fileName);

    if (file_exists($fileName)) {
        sleep(rand(5, 10));
        http_response_code(200);
        readfile($fileName);
        exit;
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST' && $URI == "/api/configurations") {
    http_response_code(204);
    exit;
} else if ($_SERVER['REQUEST_METHOD'] == 'GET' && $URI == "/api/configurations/descriptions") {
    http_response_code(200);
    readfile("ai_descriptions.json");
    exit;
}

error_log($URI);

http_response_code(404);
