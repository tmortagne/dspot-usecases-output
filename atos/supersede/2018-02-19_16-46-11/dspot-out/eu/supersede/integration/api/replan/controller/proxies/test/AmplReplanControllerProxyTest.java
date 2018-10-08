package eu.supersede.integration.api.replan.controller.proxies.test;


public class AmplReplanControllerProxyTest {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.class);

    private static eu.supersede.integration.api.replan.controller.proxies.IReplanController proxy;

    private int projectId = 1;

    @org.junit.BeforeClass
    public static void setup() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy = new eu.supersede.integration.api.replan.controller.proxies.test.eu.supersede.integration.api.replan.controller.proxies.ReplanControllerProxy();
    }

    @org.junit.Test
    public void testGetAllProjects() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Project> projects = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getAllProjects();
        org.springframework.util.Assert.notNull(projects);
        org.springframework.util.Assert.notEmpty(projects);
    }

    @org.junit.Test
    public void testGetProjectById() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Project project = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getProjectById(projectId);
        org.springframework.util.Assert.notNull(project);
    }

    @org.junit.Test
    public void testGetFeaturesOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId, eu.supersede.integration.api.replan.controller.types.FeatureStatus.PENDING);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
    }

    @org.junit.Test
    public void testGetFeatureByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(features);
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeatureByIdOfProjectById(features.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testGetReleasesOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
    }

    @org.junit.Test
    public void testGetReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Release release = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(release);
    }

    @org.junit.Test
    public void testGetFeaturesOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(releases);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(features);
    }

    @org.junit.Test
    public void testGetPlanOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Plan plan = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getPlanOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(plan);
    }

    @org.junit.Test
    public void testGetPlanOfReleaseByIdOfTenantById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById("atos");
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Plan plan = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getPlanOfReleaseByIdOfTenantById(releases.get(0).getId(), "atos");
        org.springframework.util.Assert.notNull(plan);
    }

    @org.junit.Test
    public void testGetPlanOfReleaseByIdOfTenantByIdForcingNew() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById("atos");
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Plan plan = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getPlanOfReleaseByIdOfTenantById(releases.get(0).getId(), "atos", true);
        org.springframework.util.Assert.notNull(plan);
    }

    @org.junit.Test
    public void testGetSkillsOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
    }

    @org.junit.Test
    public void testGetResourcesOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
    }

    @org.junit.Test
    public void testCreateAndDeleteProject() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Project project = createProject();
        project = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.createProject(project);
        org.springframework.util.Assert.notNull(project);
        org.springframework.util.Assert.notNull(project.getId());
        org.springframework.util.Assert.isTrue(eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteProjectById(project.getId()));
    }

    private eu.supersede.integration.api.replan.controller.types.Project createProject() {
        eu.supersede.integration.api.replan.controller.types.Project project = new eu.supersede.integration.api.replan.controller.types.Project();
        project.setName("Project Test");
        project.setDescription("Project Test Description");
        project.setEffortUnit("hour");
        project.setHoursPerEffortUnit(1.0);
        project.setHoursPerWeekFullTimeResource(40.0);
        return project;
    }

    @org.junit.Test
    public void testCreateAndDeleteFeatureForProject() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Project project = createProject();
        project = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.createProject(project);
        org.springframework.util.Assert.notNull(project);
        org.springframework.util.Assert.notNull(project.getId());
        eu.supersede.integration.api.replan.controller.types.Feature feature = createFeature();
        feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.createFeatureOfProjectById(feature, project.getId());
        org.springframework.util.Assert.notNull(feature);
        org.springframework.util.Assert.notNull(feature.getId());
        org.springframework.util.Assert.isTrue(eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteFeatureByIdOfProjectById(feature.getId(), project.getId()));
        org.springframework.util.Assert.isTrue(eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteProjectById(project.getId()));
    }

    private eu.supersede.integration.api.replan.controller.types.Feature createFeature() {
        eu.supersede.integration.api.replan.controller.types.Feature feature = new eu.supersede.integration.api.replan.controller.types.Feature();
        feature.setCode(111);
        feature.setName("Fix auto upload");
        feature.setDescription("Bla, bla, bla es mucho decir");
        feature.setEffort(4.0);
        feature.setDeadline(java.util.Calendar.getInstance().getTime());
        feature.setPriority(5);
        return feature;
    }

    @org.junit.Test
    public void testUpdateProject() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Project project = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getProjectById(projectId);
        org.springframework.util.Assert.notNull(project);
        project.setDescription(((project.getDescription()) + " modified by test"));
        project = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.updateProject(project);
        org.springframework.util.Assert.notNull(project);
    }

    @org.junit.Test
    public void testUpdateFeatureOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(features);
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeatureByIdOfProjectById(features.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
        feature.setDescription(((feature.getDescription()) + " modified by test"));
        eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.updateFeatureOfProjectById(feature, projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testUpdateReleasesOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Release release = releases.get(0);
        release.setDescription(((release.getDescription()) + " modified by test"));
        release = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.updateReleaseOfProjectById(release, projectId);
        org.springframework.util.Assert.notNull(release);
    }

    @org.junit.Test
    public void testUpdateSkillsOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
        eu.supersede.integration.api.replan.controller.types.Skill skill = skills.get(0);
        skill.setDescription(((skill.getDescription()) + " modified by test"));
        skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.updateSkillOfProjectById(skill, projectId);
        org.springframework.util.Assert.notNull(skill);
    }

    @org.junit.Test
    public void testUpdateResourceOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
        eu.supersede.integration.api.replan.controller.types.Resource resource = resources.get(0);
        resource.setDescription(((resource.getDescription()) + " modified by test"));
        resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.updateResourceOfProjectById(resource, projectId);
        org.springframework.util.Assert.notNull(resource);
    }

    @org.junit.Test
    public void testAddSkillsOfFeatureByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(features);
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillsOfFeatureByIdOfProjectById(skills, features.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testAddDependenciesOfFeatureByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        org.springframework.util.Assert.isTrue(((features.size()) > 2));
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> dependencies = new java.util.ArrayList<>();
        dependencies.add(features.get(0));
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addDependenciesOfFeatureByIdOfProjectById(dependencies, features.get(1).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testAddResourcesOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resourcesToAdd = new java.util.ArrayList<>();
        resourcesToAdd.add(resources.get(0));
        eu.supersede.integration.api.replan.controller.types.Release release = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addResourcesOfReleaseByIdOfProjectById(resourcesToAdd, releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(release);
    }

    @org.junit.Test
    public void testAddFeaturesOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> featuresToAdd = new java.util.ArrayList<>();
        featuresToAdd.add(features.get(0));
        boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addFeaturesOfReleaseByIdOfProjectById(featuresToAdd, releases.get(0).getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
    }

    @org.junit.Test
    public void testAddSkillOfProjectById() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
        skill.setName("Swift");
        skill.setDescription("Swift development of iOS");
        skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, projectId);
        org.springframework.util.Assert.notNull(skill);
    }

    @org.junit.Test
    public void testAddResourceOfProjectById() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Resource resource = new eu.supersede.integration.api.replan.controller.types.Resource();
        resource.setName("Swift Developer");
        resource.setDescription("Swift developer with experience on iOS 10");
        resource.setAvailability(80.0);
        resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addResourceOfProjectById(resource, projectId);
        org.springframework.util.Assert.notNull(resource);
    }

    @org.junit.Test
    public void testAddSkillsOfResourceByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skillsToAdd = new java.util.ArrayList<>();
        skillsToAdd.add(skills.get(0));
        eu.supersede.integration.api.replan.controller.types.Resource resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfResourceByIdOfProjectById(skillsToAdd, resources.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(resource);
    }

    @org.junit.Test
    public void testDeleteSkillsOfFeatureByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(features);
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillsOfFeatureByIdOfProjectById(skills, features.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skillsToDelete = new java.util.ArrayList<>();
        skillsToDelete.add(skills.get(0));
        feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillsOfFeatureByIdOfProjectById(skillsToDelete, feature.getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testDeleteDependenciesOfFeatureByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        org.springframework.util.Assert.isTrue(((features.size()) >= 2));
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> dependenciesToAdd = new java.util.ArrayList<>();
        dependenciesToAdd.add(features.get(1));
        eu.supersede.integration.api.replan.controller.types.Feature feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addDependenciesOfFeatureByIdOfProjectById(dependenciesToAdd, features.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> dependenciesToDelete = new java.util.ArrayList<>();
        dependenciesToDelete.add(features.get(1));
        feature = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteDependenciesOfFeatureByIdOfProjectById(dependenciesToDelete, feature.getId(), projectId);
        org.springframework.util.Assert.notNull(feature);
    }

    @org.junit.Test
    public void testDeleteResourcesOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resourcesToAdd = new java.util.ArrayList<>();
        resourcesToAdd.add(resources.get(0));
        eu.supersede.integration.api.replan.controller.types.Release release = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addResourcesOfReleaseByIdOfProjectById(resourcesToAdd, releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(release);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resourcesToDelete = new java.util.ArrayList<>();
        resourcesToDelete.add(resources.get(0));
        release = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteResourcesOfReleaseByIdOfProjectById(resourcesToDelete, release.getId(), projectId);
        org.springframework.util.Assert.notNull(release);
    }

    @org.junit.Test
    public void testDeleteFeaturesOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> features = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(releases);
        org.springframework.util.Assert.notEmpty(releases);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> featuresToAdd = new java.util.ArrayList<>();
        featuresToAdd.add(features.get(0));
        boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addFeaturesOfReleaseByIdOfProjectById(featuresToAdd, releases.get(0).getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> featuresOfRelease = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.notEmpty(features);
        int numberOfFeaturesOfRelease = featuresOfRelease.size();
        java.util.List<eu.supersede.integration.api.replan.controller.types.Feature> featuresToDelete = new java.util.ArrayList<>();
        featuresToDelete.add(features.get(0));
        result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteFeaturesOfReleaseByIdOfProjectById(featuresToDelete, releases.get(0).getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
        featuresOfRelease = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getFeaturesOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(features);
        org.springframework.util.Assert.isTrue(((featuresOfRelease.size()) == (numberOfFeaturesOfRelease - 1)));
    }

    @org.junit.Test
    public void testCancelLastPlanOfReleaseByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Release> releases = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getReleasesOfProjectById(projectId);
        org.springframework.util.Assert.notEmpty(releases);
        eu.supersede.integration.api.replan.controller.types.Plan plan = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getPlanOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(plan);
        java.lang.Boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.cancelLastPlanOfReleaseByIdOfProjectById(releases.get(0).getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
    }

    @org.junit.Test
    public void testDeleteSkillOfProjectById() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
        skill.setName("Swift");
        skill.setDescription("Swift development of iOS");
        skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, projectId);
        org.springframework.util.Assert.notNull(skill);
        boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
    }

    @org.junit.Test
    public void tesDeleteResourceOfProjectById() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Resource resource = new eu.supersede.integration.api.replan.controller.types.Resource();
        resource.setName("Swift Developer");
        resource.setDescription("Swift developer with experience on iOS 10");
        resource.setAvailability(80.0);
        resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addResourceOfProjectById(resource, projectId);
        org.springframework.util.Assert.notNull(resource);
        boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteResourceByIdOfProjectById(resource.getId(), projectId);
        org.springframework.util.Assert.isTrue(result);
    }

    @org.junit.Test
    public void testDelecteSkillsOfResourceByIdOfProjectById() throws java.lang.Exception {
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skills = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getSkillsOfProjectById(projectId);
        org.springframework.util.Assert.notNull(skills);
        org.springframework.util.Assert.notEmpty(skills);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Resource> resources = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.getResourcesOfProjectById(projectId);
        org.springframework.util.Assert.notNull(resources);
        org.springframework.util.Assert.notEmpty(resources);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skillsToAdd = new java.util.ArrayList<>();
        skillsToAdd.add(skills.get(0));
        eu.supersede.integration.api.replan.controller.types.Resource resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfResourceByIdOfProjectById(skillsToAdd, resources.get(0).getId(), projectId);
        org.springframework.util.Assert.notNull(resource);
        java.util.List<eu.supersede.integration.api.replan.controller.types.Skill> skillsToDelete = new java.util.ArrayList<>();
        skillsToDelete.add(skills.get(0));
        int numberOfSkills = resource.getSkills().size();
        resource = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillsOfResourceByIdOfProjectById(skillsToAdd, resource.getId(), projectId);
        org.springframework.util.Assert.notNull(resource);
        org.springframework.util.Assert.isTrue(((resource.getSkills().size()) == (numberOfSkills - 1)));
    }

    @org.junit.Test(timeout = 10000)
    public void testDeleteSkillOfProjectById_add622_failAssert0() throws java.lang.Exception {
        try {
            eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
            skill.setName("Swift");
            skill.setDescription("Swift development of iOS");
            skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, projectId);
            org.springframework.util.Assert.notNull(skill);
            eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), projectId);
            boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), projectId);
            org.springframework.util.Assert.isTrue(result);
            org.junit.Assert.fail("testDeleteSkillOfProjectById_add622 should have thrown IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException eee) {
        }
    }

    @org.junit.Test(timeout = 10000)
    public void testAddSkillOfProjectById_add267_add283() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
        skill.setName("Swift");
        skill.setDescription("Swift development of iOS");
        eu.supersede.integration.api.replan.controller.types.Skill o_testAddSkillOfProjectById_add267_add283__5 = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        org.junit.Assert.assertEquals("Swift development of iOS", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testAddSkillOfProjectById_add267_add283__5)).getDescription());
        org.junit.Assert.assertEquals("Swift", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testAddSkillOfProjectById_add267_add283__5)).getName());
        eu.supersede.integration.api.replan.controller.types.Skill o_testAddSkillOfProjectById_add267__5 = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        org.springframework.util.Assert.notNull(skill);
        org.junit.Assert.assertEquals("Swift development of iOS", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testAddSkillOfProjectById_add267_add283__5)).getDescription());
        org.junit.Assert.assertEquals("Swift", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testAddSkillOfProjectById_add267_add283__5)).getName());
    }

    @org.junit.Test(timeout = 10000)
    public void testDeleteSkillOfProjectById_add1393_add1420() throws java.lang.Exception {
        eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
        skill.setName("Swift");
        skill.setDescription("Swift development of iOS");
        eu.supersede.integration.api.replan.controller.types.Skill o_testDeleteSkillOfProjectById_add1393_add1420__5 = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        eu.supersede.integration.api.replan.controller.types.Skill o_testDeleteSkillOfProjectById_add1393__5 = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
        org.springframework.util.Assert.notNull(skill);
        boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), this.projectId);
        org.springframework.util.Assert.isTrue(result);
        org.junit.Assert.assertEquals("Swift", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testDeleteSkillOfProjectById_add1393_add1420__5)).getName());
        org.junit.Assert.assertEquals("Swift development of iOS", ((eu.supersede.integration.api.replan.controller.types.Skill) (o_testDeleteSkillOfProjectById_add1393_add1420__5)).getDescription());
    }

    @org.junit.Test(timeout = 10000)
    public void testDeleteSkillOfProjectById_add1393_add1423_failAssert0() throws java.lang.Exception {
        try {
            eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
            skill.setName("Swift");
            skill.setDescription("Swift development of iOS");
            eu.supersede.integration.api.replan.controller.types.Skill o_testDeleteSkillOfProjectById_add1393__5 = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
            skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
            org.springframework.util.Assert.notNull(skill);
            eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), this.projectId);
            boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), this.projectId);
            org.springframework.util.Assert.isTrue(result);
            org.junit.Assert.fail("testDeleteSkillOfProjectById_add1393_add1423 should have thrown IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException eee) {
        }
    }

    @org.junit.Test(timeout = 10000)
    public void testDeleteSkillOfProjectById_add1395_failAssert0() throws java.lang.Exception {
        try {
            eu.supersede.integration.api.replan.controller.types.Skill skill = new eu.supersede.integration.api.replan.controller.types.Skill();
            skill.setName("Swift");
            skill.setDescription("Swift development of iOS");
            skill = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.addSkillOfProjectById(skill, this.projectId);
            org.springframework.util.Assert.notNull(skill);
            eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), this.projectId);
            boolean result = eu.supersede.integration.api.replan.controller.proxies.test.AmplReplanControllerProxyTest.proxy.deleteSkillByIdOfProjectById(skill.getId(), this.projectId);
            org.springframework.util.Assert.isTrue(result);
            org.junit.Assert.fail("testDeleteSkillOfProjectById_add1395 should have thrown IllegalArgumentException");
        } catch (java.lang.IllegalArgumentException eee) {
        }
    }
}

