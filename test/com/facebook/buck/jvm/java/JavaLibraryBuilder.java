/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.jvm.java;

import static com.facebook.buck.jvm.java.JavaCompilationConstants.DEFAULT_JAVAC_OPTIONS;

import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.model.Either;
import com.facebook.buck.rules.AbstractNodeBuilder;
import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.BuildTargetSourcePath;
import com.facebook.buck.rules.PathSourcePath;
import com.facebook.buck.rules.SourcePath;
import com.facebook.buck.testutil.FakeProjectFilesystem;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.HashCode;

import java.nio.file.Path;

public class JavaLibraryBuilder extends AbstractNodeBuilder<JavaLibraryDescription.Arg> {

  private final ProjectFilesystem projectFilesystem;

  protected JavaLibraryBuilder(
      BuildTarget target,
      ProjectFilesystem projectFilesystem,
      HashCode hashCode) {
    super(
        new JavaLibraryDescription(DEFAULT_JAVAC_OPTIONS),
        target,
        projectFilesystem,
        hashCode);
    this.projectFilesystem = projectFilesystem;
  }

  public static JavaLibraryBuilder createBuilder(BuildTarget target) {
    return new JavaLibraryBuilder(target, new FakeProjectFilesystem(), null);
  }

  public static JavaLibraryBuilder createBuilder(
      BuildTarget target,
      ProjectFilesystem projectFilesystem) {
    return new JavaLibraryBuilder(target, projectFilesystem, null);
  }

  public static JavaLibraryBuilder createBuilder(BuildTarget target, HashCode hashCode) {
    return new JavaLibraryBuilder(target, new FakeProjectFilesystem(), hashCode);
  }

  public JavaLibraryBuilder addDep(BuildTarget rule) {
    arg.deps = amend(arg.deps, rule);
    return this;
  }

  public JavaLibraryBuilder addExportedDep(BuildTarget rule) {
    arg.exportedDeps = amend(arg.exportedDeps, rule);
    return this;
  }

  public JavaLibraryBuilder addProvidedDep(BuildTarget rule) {
    arg.providedDeps = amend(arg.providedDeps, rule);
    return this;
  }

  public JavaLibraryBuilder addResource(SourcePath sourcePath) {
    arg.resources = amend(arg.resources, sourcePath);
    return this;
  }

  public JavaLibraryBuilder setResourcesRoot(Path root) {
    arg.resourcesRoot = Optional.of(root);
    return this;
  }

  public JavaLibraryBuilder setMavenCoords(String mavenCoords) {
    arg.mavenCoords = Optional.of(mavenCoords);
    return this;
  }

  public JavaLibraryBuilder addSrc(SourcePath path) {
    arg.srcs = amend(arg.srcs, path);
    return this;
  }

  public JavaLibraryBuilder addSrc(Path path) {
    return addSrc(new PathSourcePath(projectFilesystem, path));
  }

  public JavaLibraryBuilder addSrcTarget(BuildTarget target) {
    return addSrc(new BuildTargetSourcePath(target));
  }

  public JavaLibraryBuilder setProguardConfig(Path proguardConfig) {
    arg.proguardConfig = Optional.of(proguardConfig);
    return this;
  }

  public JavaLibraryBuilder setCompiler(BuildRule javac) {
    SourcePath right =
        new BuildTargetSourcePath(javac.getBuildTarget());
    Either<BuiltInJavac, SourcePath> value = Either.ofRight(right);

    arg.compiler = Optional.of(value);
    return this;
  }

  public JavaLibraryBuilder setAnnotationProcessors(ImmutableSet<String> annotationProcessors) {
    arg.annotationProcessors = Optional.of(annotationProcessors);
    return this;
  }

  public JavaLibraryBuilder addTest(BuildTarget test) {
    arg.tests = amend(arg.tests, test);
    return this;
  }
}
