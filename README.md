# Gradle Git Versioner

[![MIT License](https://img.shields.io/github/license/development-and-dinosaurs/gradle-git-versioner?style=for-the-badge&logo=pagekit)](https://github.com/development-and-dinosaurs/gradle-git-versioner/blob/main/LICENSE)

## What is Git Versioner?

Git Versioner is the coolest way to automatically increase your version numbers.

With Git Versioner, you leave behind the manual process around updating your project version and have it handled
completely automatically by the plugin instead. All you have to decide is when a change should be major, minor, or patch
and Git Versioner will do the rest.

Git Versioner takes the difficulty out of manually incrementing and changing version numbers in your project, using git
commit messages and semantic versioning principles.

## How does it work?

It's really simple actually - Git Versioner will inspect your git commit history, checking for places that have been
designated as major, minor, or patch commits. It then tallies them up to come up with the current version number.

It's customisable too - you can decide what to look for to decide whether something is a major, minor, or patch update.
You can even tell it a version number to start from.

## How do I migrate to using Git Versioner?

That's really simple too.

1. Find us on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/uk.co.developmentanddinosaurs.git-versioner).

2. Apply the plugin using your preferred syntax.

3. You're ready to go - your versioning will now be taken care of by Git Versioner.

## How should I use it?

This one comes down to personal preference, but the way I like to version is as follows:

1. Start a branch with the feature I'm working on.

2. Make atomic commits for each of the changes I'm doing with useful commit messages.

3. Raise a pull request for that feature back into the main branch.

4. Merge the pull request in using a merge commit, adding the version tag to the commit message.

5. Publish from the main branch.

This ensures that you keep a nice commit history and end up with the correct version on the main branch.

## How do I use it?

You just need to change the way you're doing commits. This changes a bit depending on whether you're a new project, or a
project with an existing versioning scheme.

### I'm a new project

Great, that makes things really easy. All you have to do is make sure to tag your commits as you're making them. You can
do this on a commit by commit basis, or you can do it on merge commits. Up to you.

The default tagging is to include `[major]` for a major change, `[minor]` for a minor change, and `[patch]` for a patch
change. All other changes will be treated as `commit` changes.

### I'm an existing project doing versioning a different way

Ok, well you should stop that right now and do it this way instead. In general, you just have to follow the step above,
then do a bit extra.

You probably don't want to start from version 0.0.1 again, right? You've got two options, you can do some configuration,
or you can rewrite your history.

#### Let's do some configuration

Good idea - to use configuration, add the following block to your gradle file:

```groovy
versioner {
    startFrom {
        major = 1
        minor = 3
        patch = 5
    }
}
```

This sets your current version to 1.3.5. Obviously set this to match the version that your project is actually currently
on.

Now each time you make a major, minor, or patch commit, the calculations will be applied on top of version 1.3.5. So
your first patch commit using Git Versioner will make it version 1.3.6. The first minor commit will make it version
1.4.0, etc.

#### You mentioned rewriting history?

The nuclear option is to rewrite the project history completely and act like you were using Git Versioner all along (
Like you should have been anyway, right?).

This is really not the best idea for large or established projects, as you're going to make every single contributor sad
by pulling the history out from under them. If you only have a small or solo project, it's usually not such a big deal.

If you still want to do it, just go for a `git rebase -i <yourFirstCommit>`, mark any of the commits where you've
updated the version to be reworded, then one-by-one update the commit messages to include either `[major]`, `[minor]`,
or `[patch]`.

If you've done it right, the version produced by Git Versioner will be the same as your current version. Pretty neat
right?

## Can I use different phrases to bump the version instead?

Yes! You just need to override the match defaults using some configuration. Here's an example:

```
versioner {
  match {
    major = "+semver: major"
    minor = "+semver: minor"
    patch = "+semver: patch"
  }
}
```

## I don't really like the versioning pattern you're using

Me neither, but the default is going to stay that way to maintain backwards compatibility.

Fortunately you're able to define your own pattern for the version string. This is the pattern that will apply to
the `project.version` variable, and the git tag after the prefix. You define it like this:

```groovy
versioner {
    pattern {
        pattern = "%M.%m.%p(.%c)"
    }
}
```

That's an example of the default pattern. The pattern matching works off of simple string substitution with a little
conditional logic built in based off the commit number.

The substitutions you can use are as follows:

| pattern | description                      |
|---------|----------------------------------|
| %M      | major number                     |
| %m      | minor number                     |
| %p      | patch number                     |
| %c      | commit number                    |
| %b      | current branch                   |
| %H      | full hash of the current commit  |
| %h      | short hash of the current commit |
| ()      | show when commit number > 0      |

Here's a list of some example patterns, and the output you get for particular versions to make it more obvious how it
works:

| version | pattern             | output           |
|---------|---------------------|------------------|
| 1.2.3.4 | %M.%m.%p.%c         | 1.2.3.4          |
| 1.2.3.0 | %M.%m.%p.%c         | 1.2.3.0          |
| 1.2.3.4 | %M.%m.%p(.%c)       | 1.2.3.4          |
| 1.2.3.0 | %M.%m.%p(.%c)       | 1.2.3            |
| 1.2.3.4 | %M.%m.%p-%c         | 1.2.3-4          |
| 1.2.3.4 | %M.%m(-SNAPSHOT)    | 1.2-SNAPSHOT     |
| 1.2.3.0 | %M.%m.%p(-SNAPSHOT) | 1.2.3            |
| 1.2.3.4 | %M.%m.%p-%H         | 1.2.3-hash123456 |
| 1.2.3.4 | %M.%m.%p-%h         | 1.2.3-hash123    |
| 1.2.3.4 | %M.%m.%p-%b         | 1.2.3-main       |

Pattern calculation uses really simple string substitution and regular expressions, so there's nothing fancy like
pattern escaping or things like that.

This does mean that you can't use parentheses in your version string, and if you try to have something like
%MaybeThisIsAGoodIdea, it won't go very well.

## Can I tag things in git?

Git tagging functionality is now included within Git Versioner itself, and you can use it by simply running
the `tagVersion` task. This will create a local tag called `v<version> by default, but you can customise the version
prefix like so:

```groovy
versioner {
    tag {
        prefix = "V"
    }
}
```

This changes the tag prefix to use an uppercase 'V<version>' instead of the standard lowercase 'v<version>', but you can
be as creative as you like with this.

You can also choose to include a message with this tag. Right now the only options are to forgo a message or to use the
last commit message as the tag message. You can turn on this functionality like so:

```groovy
versioner {
    tag {
        useCommitMessage = true
    }
}
```

This is useful if you take up the habit of writing a meaningful commit message for your release commit. For example,
which is the better release message for a new piece of functionality?

```
Merge pull request #4 from feature/annotated-tags [minor]
```

```
Release: Add annotated tag messaging

This release includes the ability to specify tag messages using the message of the latest commit. 

[minor]
```

## How do I authenticate to push my tags?

The plugin supports both SSH or HTTPS to connect to your remote serve.

If you're using an SSH key to push to your remote server, your server is using an RSA host key, and you're not using a
passphrase, then you won't have to do anything. The push to the remote server will use your SSH key, and it will have no
problem connecting to your remote server.

If you're using a passphrase then you're out of luck as that isn't currently supported.

If you're using an SSH key, but your server is using an ECDSA host key, then we have a little problem. This plugin uses
JGit to interact with your git repository, which doesn't correctly handle ECDSA keys. Your options for this scenario are
to either use an RSA host key, turn off strict host key checking, or use HTTPS.

You can turn off strict host key checking in the configuration like so:

```groovy
versioner {
    git {
        authentication {
            ssh {
                strictSsl = false
            }
        }
    }
}
```

I don't really know why I called it strictSsl. I'll rename it to be strictHostKeyChecking or something like that
eventually maybe and hope nobody notices.

If you don't like the idea of turning off host checking, you can use HTTPS instead. To do this you need to make sure
your remote repository is using HTTPS rather than SSH. You can configure either username and password, or you can use an
access token. The configuration for this method looks like so:

```groovy
versioner {
    git {
        authentication {
            https {
                username = "username"
                password = "password"
                token = "token"
            }
        }
    }
}
```

If you don't want to hard code your credentials in your gradle build file, then you can pass them in as a property
instead, like this:

```groovy
versioner {
    git {
        authentication {
            https {
                token = project.findProperty("token")
            }
        }
    }
}
```

## My version is "unspecified"

This is a common problem stemming from the configuration and build phases in Gradle, which can trip a lot of people up.
This is ok though, because we can work around this to solve the problem.

First though, a little history lesson. I built the original architecture for this plugin with the java plugin in mind-
namely the `jar` task so that project artefacts could be versioned more easily. The jar task doesn't resolve the version
until the execution stage, so it was safe to set the version at the end of the configuration stage- just in time for the
jar task.

Unfortunately this makes it more difficult if you want to access the version in the configuration stage, as it won't
have been set yet. There are a couple of things you can do:

1. Don't access the version in the configuration stage, and access it from the execution stage instead. If you're
   writing your own task, this means using the version in the `doFirst` or `doLast` closures instead of the
   configuration body.

2. Access it at the end of the configuration stage by wrapping your configuration in a `project.afterEvaluate` block.
   This is how the version gets set in the first place, so should work correctly.

3. Manually apply the version before you need to use it. You can call the `apply()` method on the `versioner` extension
   to force resolution of the version and make it available from the point that you make the call.

Here are some examples of when to access the version and what it will be set to:

```groovy
task version() {
    println project.version // prints unspecified - this is too early in the configuration phase
    project.afterEvaluate {
        println project.version // prints version - configuration stage but late enough to be calculated
    }
    doFirst {
        println project.version // prints version - execution stage, so late enough to be calculated
    }
    doLast {
        println project.version // prints version - execution stage, so late enough to be calculated
    }
}

versioner.apply()

task version2() {
    println project.version // prints version - version has been applied before this line is evaluated
}
```

## This is so cool, how do I contribute?

I know right? You should check out the [contribution guide](CONTRIBUTING.md).
