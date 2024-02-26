package com.laura.playground.utils;

public enum Ease {
    IN(new Ease.EaseType.EaseIn(2.0D)),
    IN_2(new Ease.EaseType.EaseIn(3.0D)),
    IN_3(new Ease.EaseType.EaseIn(4.0D)),
    IN_SINE(new Ease.EaseType.EaseInSine(), true),
    IN_CIRC(new Ease.EaseType.EaseInCirc(), true),
    OUT(new Ease.EaseType.EaseOut(2.0D)),
    OUT_2(new Ease.EaseType.EaseOut(3.0D)),
    OUT_3(new Ease.EaseType.EaseOut(4.0D)),
    OUT_SINE(new Ease.EaseType.EaseOutSine(), true),
    OUT_CIRC(new Ease.EaseType.EaseOutCirc(), true),
    IN_OUT(new Ease.EaseType.EaseInOut(2.0D)),
    IN_OUT_2(new Ease.EaseType.EaseInOut(3.0D)),
    IN_OUT_3(new Ease.EaseType.EaseInOut(4.0D)),
    IN_OUT_SINE(new Ease.EaseType.EaseInOutSine(), true),
    IN_OUT_CIRC(new Ease.EaseType.EaseInOutCirc(), true),
    NONE(new Ease.EaseType.EaseNone(), true);

    private final Ease.AnimationEaseType ease;
    private final boolean immutable;

    private Ease(Ease.AnimationEaseType ease) {
        this(ease, false);
    }

    private Ease(Ease.AnimationEaseType ease, boolean immutable) {
        this.ease = ease;
        this.immutable = immutable;
    }

    public double ease(double min, double max, double ratio) {
        return this.ease.ease(min, max, ratio);
    }

    public double customEase(double min, double max, double ratio, double power) {
        return customEase(min, max, ratio, power, this);
    }

    public double blend(Ease blendingEase, double min, double max, double ratio) {
        return this.blend(blendingEase, min, max, ratio, 0.5D);
    }

    public double blend(Ease blendingEase, double min, double max, double ratio, double blendRatio) {
        double easePrimeResult = this.ease(min, max, ratio);
        double easeBlendResult = blendingEase.ease(min, max, ratio);
        return Mathsf.lerp(easePrimeResult, easeBlendResult, blendRatio);
    }

    public static Ease getDefault() {
        return NONE;
    }

    public static double customEase(double min, double max, double ratio, double power, Ease template) {
        return (new Ease.EaseType.CustomEase(power, template)).ease(min, max, ratio);
    }

    public static double blend(Ease ease1, Ease ease2, double min, double max, double ratio) {
        return blend(ease1, ease2, min, max, ratio, 0.5D);
    }

    public static double blend(Ease ease1, Ease ease2, double min, double max, double ratio, double blendRatio) {
        return ease1.blend(ease2, min, max, ratio, blendRatio);
    }

    public boolean isImmutable() {
        return this.immutable;
    }

    private interface AnimationEaseType {
        double ease(double min, double max, double ratio);
    }

    private static final class EaseType {
        private static final class EaseNone implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                return Mathsf.lerp(min, max, ratio);
            }
        }

        private static final class EaseInOutCirc implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                double mid = Mathsf.mid(min, max);
                return ratio > 0.5D ?
                        (new Ease.EaseType.EaseInCirc()).ease(mid, max, (ratio - 0.5D) * 2.0D) :
                        (new Ease.EaseType.EaseOutCirc()).ease(min, mid, ratio * 2.0D);
            }
        }

        private static final class EaseInOutSine implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                double lerpValue = Mathsf.lerp(0.0D, 180.0D, ratio);
                double lerpValueRads = Math.toRadians(lerpValue);
                if (lerpValue == 0.0D) {
                    lerpValueRads = 0.0D;
                }

                return Mathsf.lerp(min, max, 1.0D - Mathsf.iLerp(-1.0D, 1.0D, Math.cos(lerpValueRads)));
            }
        }

        private static final class EaseInOut implements Ease.AnimationEaseType {
            private final double power;

            EaseInOut(double power) {
                this.power = power;
            }

            public double ease(double min, double max, double ratio) {
                double mid = Mathsf.mid(min, max);
                return ratio > 0.5D ?
                        (new Ease.EaseType.EaseIn(this.power)).ease(mid, max, (ratio - 0.5D) * 2.0D) :
                        (new Ease.EaseType.EaseOut(this.power)).ease(min, mid, ratio * 2.0D);
            }
        }

        private static final class EaseOutCirc implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                return Mathsf.lerp(max, min, Math.abs(Math.sin(Math.acos(ratio))));
            }
        }

        private static final class EaseOutSine implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                double lerpValue = Mathsf.lerp(0.0D, 90.0D, ratio);
                double lerpValueRads = Math.toRadians(lerpValue);
                if (lerpValue == 0.0D) {
                    lerpValueRads = 0.0D;
                }

                return Mathsf.lerp(min, max, 1.0D - Math.abs(Math.cos(lerpValueRads)));
            }
        }

        private static final class EaseOut implements Ease.AnimationEaseType {
            private final double power;

            EaseOut(double power) {
                this.power = power;
            }

            public double ease(double min, double max, double ratio) {
                return Mathsf.lerp(min, max, Math.pow(ratio, this.power));
            }
        }

        private static final class EaseInCirc implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                return Mathsf.lerp(min, max, Math.abs(1.0D - Math.sin(Math.acos(ratio))));
            }
        }

        private static final class EaseInSine implements Ease.AnimationEaseType {
            public double ease(double min, double max, double ratio) {
                double lerpValue = Mathsf.lerp(0.0D, 90.0D, ratio);
                double lerpValueRads = Math.toRadians(lerpValue);
                if (lerpValue == 180.0D) {
                    lerpValueRads = 0.0D;
                }

                return Mathsf.lerp(min, max, Math.abs(Math.sin(lerpValueRads)));
            }
        }

        private static final class EaseIn implements Ease.AnimationEaseType {
            private final double power;

            EaseIn(double power) {
                this.power = power;
            }

            public double ease(double min, double max, double ratio) {
                return Mathsf.lerp(max, min, Math.pow(1.0D - ratio, this.power));
            }
        }

        private static final class CustomEase implements Ease.AnimationEaseType {
            private final Ease template;
            private final double power;

            CustomEase(double power, Ease template) {
                this.power = power;
                this.template = template;
            }

            public double ease(double min, double max, double ratio) {
                if (this.template == null) {
                    return Ease.NONE.ease(min, max, ratio);
                } else if (this.template.isImmutable()) {
                    return this.template.ease(min, max, ratio);
                } else {
                    switch (this.template) {
                        case IN:
                        case IN_2:
                        case IN_3:
                            return (new Ease.EaseType.EaseIn(this.power)).ease(min, max, ratio);
                        case OUT:
                        case OUT_2:
                        case OUT_3:
                            return (new Ease.EaseType.EaseOut(this.power)).ease(min, max, ratio);
                        case IN_OUT:
                        case IN_OUT_2:
                        case IN_OUT_3:
                            return (new Ease.EaseType.EaseInOut(this.power)).ease(min, max, ratio);
                        default:
                            return this.template.ease(min, max, ratio);
                    }
                }
            }
        }
    }
}
