rootProject.name = "GlitchInventoryAPI"
include("plugin")
include("core")
include("core:v1_19_R2")
findProject(":core:v1_19_R2")?.name = "v1_19_R2"
include("core:v1_18_R2")
findProject(":core:v1_18_R2")?.name = "v1_18_R2"
include("core:v1_17_R1")
findProject(":core:v1_17_R1")?.name = "v1_17_R1"
