package smd.ufc.br.easycontext.fence.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smd.ufc.br.easycontext.fence.Fence;

public class AggregateParameter implements FenceParameter{
    List<Fence> fences;

    private AggregateParameter(List<Fence> fences){
        this.fences = fences;
    }

    public List<Fence> getFences() {
        return fences;
    }

    public static class Builder{
        List<Fence> fences;

        public Builder(){
            this.fences = new ArrayList<>();
        }
        public Builder addFence(Fence fence){
            this.fences.add(fence);
            return this;
        }
        public Builder addFences(List<Fence> fences){
            this.fences.addAll(fences);
            return this;
        }

        public Builder addFences(Fence... fences){
            this.fences.addAll(Arrays.asList(fences));
            return this;
        }

        public AggregateParameter build(){
            return new AggregateParameter(this.fences);
        }
    }
}
