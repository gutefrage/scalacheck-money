import com.thoughtworks.sbtApiMappings.ApiMappings

import sbt._

object ScalacheckApiMappingRule extends AutoPlugin {
  import ApiMappings.autoImport._

  override val requires = ApiMappings

  override val trigger = allRequirements

  private def rule: PartialFunction[ModuleID, URL] = {
    case ModuleID("org.scalacheck", _, version, _, _, _, _, _, _, _, _) =>
      url(
        s"http://www.scalacheck.org/files/scalacheck_2.11-$version-api/index.html")
  }

  override def projectSettings =
    apiMappingRules := rule.orElse(apiMappingRules.value)
}
