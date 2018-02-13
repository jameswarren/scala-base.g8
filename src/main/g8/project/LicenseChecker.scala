import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtLicenseReport.autoImport._
import sbtassembly.AssemblyPlugin.autoImport.assembly

/**
  * sbt definitions that require all software dependencies to have approved software licenses
  * else will fail assembly build
  */
object LicenseChecker {

  lazy val invalidLicenses: SettingKey[Seq[LicenseCategory]] =
    settingKey[Seq[LicenseCategory]]("incompatible software licenses")

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    invalidLicenses := List(
      LicenseCategory.NoneSpecified, LicenseCategory.Unrecognized, LicenseCategory.GPL
    ),
    updateLicenses := {
      val licenseReport = updateLicenses.value
      val prohibited = invalidLicenses.value

      licenseReport.licenses.find {
        l => prohibited contains l.license.category
      } match {
        case None =>
          licenseReport
        case Some(l) =>
          sys.error(s"license check failed for module: \${l.module}")
      }
    },
    packageBin in Compile := {
      dumpLicenseReport.value
      (packageBin in Compile).value
    },
    assembly := {
      dumpLicenseReport.value
      assembly.value
    }
  )
}