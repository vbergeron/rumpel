ThisBuild / tlBaseVersion := "0.1" // your current series x.y

ThisBuild / organization     := "io.funktional"
ThisBuild / organizationName := "FunktionalIO"
ThisBuild / startYear        := Some(2024)
ThisBuild / licenses         := Seq("EPL-2.0" -> url("https://www.eclipse.org/legal/epl-2.0/"))
ThisBuild / developers ++= List(
  // your GitHub handle and name
  tlGitHubDev("rlemaitre", "Raphaël Lemaitre")
)

//// false by default, set to true to publish to oss.sonatype.org
//ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

val Scala3 = "3.3.4"
ThisBuild / scalaVersion := Scala3 // the default Scala

ThisBuild / githubWorkflowOSes         := Seq("ubuntu-latest")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))

ThisBuild / tlCiHeaderCheck          := true
ThisBuild / tlCiScalafmtCheck        := true
ThisBuild / tlCiMimaBinaryIssueCheck := true
ThisBuild / tlCiDependencyGraphJob   := true
ThisBuild / autoAPIMappings          := true

val sharedSettings = Seq(
  organization   := "io.funktional",
  name           := "rumpel",
  version        := "0.1-SNAPSHOT",
  scalaVersion   := "3.3.4",
  libraryDependencies ++= Seq(
    "org.scalameta" %%% "munit" % "1.0.2" % Test
  ),
  // Headers
  headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.cppStyleLineComment),
  headerLicense  := Some(HeaderLicense.Custom(
    """|Copyright (c) 2024-2024 by Raphaël Lemaitre and Contributors
               |This software is licensed under the Eclipse Public License v2.0 (EPL-2.0).
               |For more information see LICENSE or https://opensource.org/license/epl-2-0
               |""".stripMargin
  ))
)

lazy val root =
    project
        .in(file("."))
        .aggregate(rumpel.js, rumpel.jvm, rumpel.native, unidocs)
        .settings(sharedSettings)
        .settings(
          publish      := {},
          publishLocal := {}
        )

lazy val rumpel  =
    crossProject(JSPlatform, JVMPlatform, NativePlatform)
        .withoutSuffixFor(JVMPlatform)
        .crossType(CrossType.Pure)
        .in(file("core"))
        .settings(sharedSettings)
        .jvmSettings(
          // Add JVM-specific settings here
        ).jsSettings(
          // Add JS-specific settings here
          scalaJSUseMainModuleInitializer := true
        ).nativeSettings(
          // Add Native-specific settings here
        )
lazy val unidocs = project
    .in(file("unidocs"))
    .enablePlugins(TypelevelUnidocPlugin)
    .settings(
      name                                       := "rumpel-docs",
      ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(rumpel.jvm)
    )
    .settings(sharedSettings)
