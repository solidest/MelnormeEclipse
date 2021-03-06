package LANG_PROJECT_ID.ide.core.operations;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.ToolMarkersHelper;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation.BuildOperationParameters;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.bundle.BuildTargetNameParser2;
import melnorme.lang.tooling.bundle.BundleInfo;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public final class LANGUAGE_BuildManager extends BuildManager {
	
	protected static final String NORMAL_BUILD = "build";
	protected static final String CHECK_BUILD = "check";

	public LANGUAGE_BuildManager(LangBundleModel bundleModel, ToolManager toolManager) {
		super(bundleModel, toolManager);
	}
	
	@Override
	protected Indexable<BuildType> getBuildTypes_do() {
		return ArrayList2.create(
			new LANGUAGE_BuildType(NORMAL_BUILD),
			new LANGUAGE_CheckBuildType(CHECK_BUILD)
		);
	}
	
	public static class LANGUAGE_BuildType extends BuildType {
		public LANGUAGE_BuildType(String name) {
			super(name);
		}
		
		@Override
		public String getDefaultCommandArguments(BuildTarget bt) throws CommonException {
			return "build ."; // TODO: LANG
		}
		
		@Override
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			return new LaunchArtifact(bt.getBuildConfigName(), "default_artifact.exe"); // TODO: LANG
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams)
				throws CommonException {
			return new LANGUAGE_BuildTargetOperation(buildOpParams);
		}
	}
	
	public class LANGUAGE_CheckBuildType extends LANGUAGE_BuildType {
		public LANGUAGE_CheckBuildType(String name) {
			super(name);
		}
		
		@Override
		public String getDefaultCommandArguments(BuildTarget bt) throws CommonException {
			return "check .";
		}
	}
	
	@Override
	public BuildTargetNameParser getBuildTargetNameParser() {
		return new BuildTargetNameParser2();
	}
	
	@Override
	protected void addDefaultBuildTarget(ArrayList2<BuildTarget> buildTargets, IProject project, BundleInfo bundleInfo,
			BuildConfiguration buildConfig, BuildType buildType, BuildTargetData btd) {
		if(buildType.getName().equals(CHECK_BUILD)) {
			btd.autoBuildEnabled = true;
		}
		super.addDefaultBuildTarget(buildTargets, project, bundleInfo, buildConfig, buildType, btd);
	}
	
	/* ----------------- Build ----------------- */
	
	protected static class LANGUAGE_BuildTargetOperation extends BuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(BuildOperationParameters buildOpParams) {
			super(buildOpParams);
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult processResult, IOperationMonitor om) 
				throws CommonException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process build result
			
			new ToolMarkersHelper().addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation2(project), om);
		}
	}
	
}