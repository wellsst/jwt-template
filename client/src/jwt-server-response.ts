interface JwtServerResponse {
  jwt?: string;
  challengeId?: string;
  cleanupOlderThan?: string;
}
