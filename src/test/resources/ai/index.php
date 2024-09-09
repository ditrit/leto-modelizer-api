<?php
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(404);
    exit;
}

$uriSegments = explode('/', trim($_SERVER['REQUEST_URI'], '/'));
$type = isset($uriSegments[1]) ? $uriSegments[1] : null;
$requestBody = json_decode(file_get_contents('php://input'), true);

error_log($type);
error_log(file_get_contents('php://input'));

if ($type == "chat") {
    $contextValue = isset($requestBody["context"]) ? (int) $requestBody["context"] : 0;
    $data = [
        "context" =>1 + $contextValue,
        "message" => "OK"
    ];
    header('Content-Type: application/json');
    $json = json_encode($data);
    echo $json;
    exit;
} else if ($type && isset($requestBody['pluginName'])) {
    $fileName = $type . "_" . str_replace("@ditrit/", "", $requestBody['pluginName']) . ".json";
    error_log($fileName);

    if (file_exists($fileName)) {
        sleep(rand(5, 10));
        http_response_code(200);
        readfile($fileName);
        exit;
    }
}

http_response_code(400);
