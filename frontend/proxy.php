<?php

$path = $_GET['path'];

$session = curl_init( $path );

curl_setopt( $session, CURLOPT_HEADER, false );
curl_setopt( $session, CURLOPT_RETURNTRANSFER, true );

$xml = curl_exec( $session );

curl_close( $session );

$xml_parser = xml_parser_create();
xml_parse_into_struct( $xml_parser, $xml, $vals );
xml_parser_free( $xml_parser );

header("Content-Type: application/json");
print $vals[ 0 ][ "value" ];

?>