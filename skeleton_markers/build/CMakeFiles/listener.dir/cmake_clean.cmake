FILE(REMOVE_RECURSE
  "../msg_gen"
  "../src/skeleton_markers/msg"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/listener.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
