# Gradle Git Versioner

## What is Git Versioner?
Git Versioner is the coolest way to automatically increase your version numbers.

With Git Versioner, you can leave behind the manual process around updating the project version and have it handled completely automatically instead. All you have to decide is when a change should be major, minor, or patch. Git Versioner will do the rest.

Git Versioner takes the difficulty out of manually incrementing and changing version numbers in your project, using git commit messages and semantic versioning principles.

## How does it work?
It's really simple actually- Git Versioner will inspect your git commit history, checking for places that have been designated as major, minor, or patch commits. It then tallies them up to come up with the current version number.

It's customisable too- you can decide what to look for to decide whether something is a major, minor, or patch update. You can even tell it a version number to start from.

## How do I migrate to using Git Versioner?
That's really simple too. Find us on the [Gradle Plugins Portal](https://plugins.gradle.org/plugin/io.toolebox.git-versioner), apply the plugin using your preferred syntax and you're ready to go- your versioning will now be taken care of by Git Versioner.

## How do I use it?
You just need to change the way you're doing commits, but it changes a bit depending on whether you're a new project or a projet with an existing versioning scheme.

### I'm a new project
Great, that makes things really easy. All you have to do is make sure to tag your commits as you're making them. You can do this on a commit by commit basis, or you can do it on merge commits. Up to you. The default tagging is to include [major] for a major change. [minor] for a minor change, and [patch] for a patch change. All other changes will be treated as commit changes.

If you don't like [major], [minor], and [patch], you can customise what to look for by using a configuration block.

```
versioner {
  match {
    major = '[trex]'
    minor = '[stego]'
    patch ='[compy]'
  }
}
```

Or maybe something a bit more sensible. I don't know, I like [major], [minor], [patch].

### I don't really like the versioning pattern you're using 
Yeah me neither, but the default is going to stay that way to maintain backwards compatibility. 

Fortunately you're able to define your own pattern for the version string. This is the pattern that will apply to the `project.version` variable, and the git tag after the prefix. You define it like this: 

```
versioner {
  pattern {
    pattern = "%M.%m.%p(.%c)"
  }
}
```

That's an example of the default pattern. The pattern matching works off of simple string substitution with a little bit of conditional logic built in based off the commit number. 

The substitutions you can use are as follows: 

| pattern | description                         |
| ------- | ----------------------------------- |
| %M      | major number                        |
| %m      | minor number                        |
| %p      | patch number                        |
| %c      | commit number                       |
| %b      | current branch                      |
| %h      | short hash of the current commit    |
| ()      | only appears when commit number > 0 |

Some example patterns are listed below: 

| version | pattern             | output         |
| ------- | ------------------- | -------------- |
| 1.2.3.4 | %M.%m.%p.%c         | 1.2.3.4        |
| 1.2.3.0 | %M.%m.%p.%c         | 1.2.3.0        |
| 1.2.3.4 | %M.%m.%p(.%c)       | 1.2.3.4        |
| 1.2.3.0 | %M.%m.%p(.%c)       | 1.2.3          |
| 1.2.3.4 | %M.%m.%p-%c         | 1.2.3-4        |
| 1.2.3.4 | %M.%m.%p(-SNAPSHOT) | 1.2.3-SNAPSHOT |
| 1.2.3.0 | %M.%m.%p(-SNAPSHOT) | 1.2.3          |
| 1.2.3.4 | %M.%m.%p-%h         | 1.2.3-hash123  |
| 1.2.3.4 | %M.%m.%p-%b         | 1.2.3-master   |


### I'm currently doing versioning a different way
Ok, well you should stop that right now and do it this way instead. In general you just have to follow the step above, then do a bit extra.

You probably don't want to start from version 0.0.1 again, right? You've got two options, you can do some configuration, or you can rewrite your history.

To use configration, add the following block to your gradle file.

```
versioner {
  startFrom {
    major = 1
    minor = 3
    patch = 5
  }
}
```

Now each time you make a major, minor, or patch commit, the calculations will be applied on top of version 1.3.5. So your first patch commit using Git Versioner will make it version 1.3.6, etc.

The nuclear option is to rewrite the project history completely and act like you were using Git Versioner all along (Like you should have been anyway, right?).

This is really not the best idea for large or established projects, as you're going to make every single contributor sad by pulling the history out from under them. But for small or solo projects, it's not such a big deal.

If you still want to do it, just go for a `git rebase -i <yourFirstCommit>`, mark any of the commits where you've updated the version to be reworded, then one-by-one update the commit messages to include either [major], [minor], or [patch]. If you've done it right, the version produced by Git Versioner will be the same as your current version. Pretty neat right?

## Can I tag things in git so I know what version I'm on?
Yes! So originally we would have recommended you do it yourself by running something like
`./gradlew -q printVersion || xargs git tag && git push --tags`, which if the syntax is correct I think will probably
create a local tag of the current version, then push it up.

If that sounds like a lot of work to add to all of your repositories, then you will like what we've come up with.
Git tagging functionality is now included within Git Versioner itself, and you can use it by simply running the
`tagVersion` task. This will create a local tag called `v<version> by default, but you can customise the version
prefix like so:
```
versioner {
  tag {
    prefix = 'V'
  }
}
```
This changes the tag prefix to use an uppercase 'V<version>' instead of the standard lowercase 'v<version>', but you
can be as creative as you like with this.

## How do I authenticate to push my tags?
You'll either be using SSH or HTTPS to connect to your remote server, and both methods are supported.

If you're using an SSH key to push to your remote server, your server is using an RSA host key, and you're
not using a passphrase, then you won't have to do anything. The push to the remote server will use your SSH
key and it will have no problem connecting to your remote server.

If you're using a passphrase then you're out of luck as that isn't currently supported, but it probably
will be in the next version and will look something like this:
```
versioner {
  git {
    authentication {
      ssh {
        passphrase = 'mypass'
      }
    }
  }
}
```
If you're using an SSH key, but your server is using an ECDSA host key, then we have a little bit of a problem.
This plugin uses JGit to interact with your git repository, which doesn't correctly handle ECDSA keys. Your
options for this scenario are to either use an RSA host key, turn off strict host key checking, or use HTTPS.

You can turn off strict host key checking in the configuration like so:
```
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
Yeah I don't really know why I called it strictSsl. I'll rename it to be strictHostKeyChecking or
something like that in the next version and hope nobody notices.

If you don't like the idea of turning off the host checking, you can use HTTPS instead. To do this you need to
make sure your remote repository is using HTTPS rather than SSH. You can configure either username and password,
or you can use an access token. The configuration for this looks like so:
```
versioner {
  git {
    authentication {
      https {
        username = 'username'
        password = 'password'
        token = 'token'
      }
    }
  }
}
```
If you don't want to hard code your credentials in your gradle build file, then you can pass them in as a
property instead, like so:
```
ext.token = project.hasProperty('token') ? token : ''
versioner {
  git {
    authentication {
      https {
        token = ext.token
      }
    }
  }
}
```
## This is so cool, how do I contribute?
I know right? You should check out the [contribution guide](CONTRIBUTING.md).
