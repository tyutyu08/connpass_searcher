# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Don't let testing shortcuts get into master by accident
fail("fdescribe left in tests") if `grep -r fdescribe specs/ `.length > 1
fail("fit left in tests") if `grep -r fit specs/ `.length > 1

# ラベルをつける
is_to_release = github.branch_for_base.match(/release\/v[0-9]+\.[0-9]+\.[0-9]/)
release_version = github.branch_for_base.match(/[0-9]+\.[0-9]+\.[0-9]/)

if is_to_release
  pr_number = github.pr_json["number"]
  auto_label.set(pr_number, "Ver"+release_version, "ff8800")
end