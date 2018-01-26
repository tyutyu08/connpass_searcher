# A sample Guardfile
# More info at https://github.com/guard/guard#readme

guard :gradle do
  watch(%r{^app/src/main/(.+)\.*$}) { |m| m[1].split('.')[0].split('/')[-1] }
  watch(%r{^app/src/test/(.+)\.*$}) { |m| m[1].split('.')[0].split('/')[-1] }
  watch('build.gradle')
end
