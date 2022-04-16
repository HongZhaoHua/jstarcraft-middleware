# JStarCraft Middleware

****

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total lines](https://tokei.rs/b1/github/HongZhaoHua/jstarcraft-middleware?category=lines)](https://tokei.rs/b1/github/HongZhaoHua/jstarcraft-middleware?category=lines)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8e39a24e1be740c58b83fb81763ba317)](https://www.codacy.com/project/HongZhaoHua/jstarcraft-middleware/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=HongZhaoHua/jstarcraft-middleware&amp;utm_campaign=Badge_Grade_Dashboard)

希望路过的同学,顺手给JStarCraft框架点个Star,算是对作者的一种鼓励吧!

## 目录

* [介绍](#介绍)
* [架构](#架构)
* [模块](#模块)
* [安装](#安装)
* [使用](#使用)
* [概念](#概念)
* [特性](#特性)
* [示例](#示例)
* [对比](#对比)
* [版本](#版本)
* [参考](#参考)
* [协议](#协议)
* [作者](#作者)
* [致谢](#致谢)
* [捐赠](#捐赠)

****

## 介绍

**JStarCraft Middleware是一个面向中间件的轻量级框架,遵循Apache 2.0协议.**

目标是提供一个通用的中间件抽象层,作为项目的基础.

让相关领域的研发人员能够在各种实现层之间无缝切换.

涵盖了配置中间件,治理中间件,监控中间件,网关中间件和事务中间件5个方面.

****

## 架构

JStarCraft Middleware框架各个模块之间的关系:

| 模块 | 功能 | 依赖 |
| :----: | :----: | :----: |
| middleware-gate | 提供各种网关中间件适配 | core-common |
| middleware-governance | 提供各种治理中间件适配 | core-common |
| middleware-monitor | 提供各种监控中间件适配 | core-common |
| middleware-profile | 提供各种配置中间件适配 | core-common |

****

## 特性

* 网关中间件(middleware-gate)
    * Gateway
    * Zuul
* 治理中间件(middleware-governance)
    * Consul
    * etcd
    * Eureka
    * Kubernetes
    * Nacos
    * Redis
    * ZooKeeper
* 监控中间件(middleware-monitor)
    * Jaeger
    * Pinpoint
    * SkyWalking
    * Zipkin
* 配置中间件(middleware-profile)
    * Apollo
    * Archaius
    * Config
    * Consul
    * etcd
    * Nacos
    * Redis
    * ZooKeeper

****

## 安装

****

## 使用

****

## 概念

****

## 示例

****

## 对比

****

## 版本

****

## 参考

****

## 协议

****

## 作者

| 作者 | 洪钊桦 |
| :----: | :----: |
| E-mail | 110399057@qq.com, jstarcraft@gmail.com |

****

## 致谢

****

## 捐赠

****
