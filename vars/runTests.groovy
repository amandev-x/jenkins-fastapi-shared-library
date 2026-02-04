#!/usr/bin/env groovy 

/**
 * Run tests with coverage
 * @param coverageThreshold - Minimum coverage percentage (default: 80)
 */

 def call(Map config = [:]) {
  def coverageThreshold = config.coverageThreshold ?: 80
  def testPath = config.testPath ?: 'tests'

  echo "Running tests with ${coverageThreshold} coverage requirement"

  sh """
  . venv/bin/activate
  pytest ${testPath}
  --cov=app \
  --cov-report=term-missing \
  --cov-report=html \
  --cov-fail-under=${coverageThreshold}
  --junitxml=test-results.xml
  """

  echo "Test coverage passed with coverage >= ${coverageThreshold}%"
 }