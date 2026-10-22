{
  description = "Java Spring boot";

  inputs = {
    nixpkgs.url = "github:Nixos/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk17
          maven
          spring-boot-cli
        ];

        shellHook = ''
          echo "Spring Boot DevShell Active!"
          java -version
          mvn -version
        '';
      };
    };
}
