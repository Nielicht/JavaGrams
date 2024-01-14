with (import <nixpkgs> {});

let
  libPath = lib.makeLibraryPath [
    xorg.libXxf86vm
    xorg.libXtst
    libGL
    alsaLib
  ];
in
  mkShell {
    buildInputs = [
      jetbrains.idea-ultimate
      jdk21
    ];

    shellHook = ''
      export LD_LIBRARY_PATH="${libPath}:$LD_LIBRARY_PATH"
      export JAVA_HOME="${jdk21.home}"
    '';
  }