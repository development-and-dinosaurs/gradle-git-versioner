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
Great, that makes things really easy. All you have to do is make sure to tag your commits as you're making them. You can do this on a commit by commit basis, or you can do it on merge commits. Up to you. The default tagging is to include [major] for a major change. [minor] for a minor change, and [patch] for a patch change. All other changes will be treated as build changes. 

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

## This is so cool, how do I contribute? 
Check out the [contribution guide](CONTRIBUTING.md)
